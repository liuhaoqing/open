package com.liuhq.open.service.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.liuhq.open.constant.RequestUrlEnum;
import com.liuhq.open.model.CourseDTO;
import com.liuhq.open.model.VideoDTO;
import com.liuhq.open.model.VideoLearnProgressDTO;
import com.liuhq.open.service.CourseService;
import com.liuhq.open.utils.WebClientFactory;

public class CourseServiceImpl implements CourseService {

	private WebClient client = WebClientFactory.getInstance();

	@Override
	public List<CourseDTO> getAll() {
		var list = new ArrayList<CourseDTO>();
		try {
			var request = WebRequest.newAboutBlankRequest();
			request.setUrl(new URL(RequestUrlEnum.GET_MY_COURSE.getUrl() + "?StatusCode=1"));
			request.setHttpMethod(RequestUrlEnum.GET_MY_COURSE.getMethod());
			var response = client.loadWebResponse(request);
			var result = response.getContentAsString();
			var dataJSONArray = JSONObject.parseObject(result).getJSONArray("data");
			for (int i = 0; i < dataJSONArray.size(); i++) {
				var dataJSONObject = dataJSONArray.getJSONObject(i);
				list.add(CourseDTO.builder().courseName(dataJSONObject.getString("CourseName"))
						.courseCode(dataJSONObject.getString("CourseCode"))
						.courseImg(dataJSONObject.getString("CourseImg"))
						.courseWareCount(dataJSONObject.getLong("CourseWareCount"))
						.isTrue(dataJSONObject.getBoolean("IsTrue")).progress(dataJSONObject.getBigDecimal("Progress"))
						.isRedirect(dataJSONObject.getBoolean("IsRedirect"))
						.homeWorkUrl(dataJSONObject.getString("HomeWorkUrl"))
						.studentCourseID(dataJSONObject.getLong("StudentCourseID"))
						.courseID(dataJSONObject.getLong("CourseID")).choosedate(dataJSONObject.getDate("choosedate"))
						.courseStatus(dataJSONObject.getString("CourseStatus"))
						.teachPlanID(dataJSONObject.getLong("TeachPlanID"))
						.statusCode(dataJSONObject.getString("StatusCode"))
						.creditHour(dataJSONObject.getLong("CreditHour"))
						.studyCourseModeName(dataJSONObject.getString("StudyCourseModeName"))
						.studentCode(dataJSONObject.getString("StudentCode"))
						.bookingExamNo(dataJSONObject.getLong("BookingExamNo"))
						.beginStudyTime(dataJSONObject.getDate("BeginStudyTime"))
						.validPeriod(dataJSONObject.getLong("validPeriod")).term(dataJSONObject.getLong("Term"))
						.build());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<VideoLearnProgressDTO> listVideoLearnProgress(Long courseID) {
		var list = new ArrayList<VideoLearnProgressDTO>();
		try {
			var request = WebRequest.newAboutBlankRequest();
			request.setUrl(new URL(RequestUrlEnum.GET_VIDEO_LEARN_PROGRESS.getUrl() + "?courseID=" + courseID));
			request.setHttpMethod(RequestUrlEnum.GET_VIDEO_LEARN_PROGRESS.getMethod());
			var response = client.loadWebResponse(request);
			var result = response.getContentAsString();
			var dataJSONArray = JSONObject.parseObject(result).getJSONArray("data");
			if (dataJSONArray != null && !dataJSONArray.isEmpty()) {
				for (int i = 0; i < dataJSONArray.size(); i++) {
					var dataJSONObject = dataJSONArray.getJSONObject(i);
					list.add(VideoLearnProgressDTO.builder().courseID(dataJSONObject.getLong("CourseID"))
							.resourceID(dataJSONObject.getString("ResourceID")).data(dataJSONObject.getLong("Data"))
							.type(dataJSONObject.getLong("Type")).createDate(dataJSONObject.getDate("CreateDate"))
							.totalTimes(dataJSONObject.getLong("TotalTimes")).build());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<VideoDTO> listAllVideo(Long courseID) {
		var list = new ArrayList<VideoDTO>();
		try {
			var request = WebRequest.newAboutBlankRequest();
			request.setUrl(new URL(RequestUrlEnum.GET_COURSE_WARE_JSON_BY_RS.getUrl() + "?courseid=" + courseID));
			request.setHttpMethod(RequestUrlEnum.GET_COURSE_WARE_JSON_BY_RS.getMethod());
			var response = client.loadWebResponse(request);
			var result = response.getContentAsString();
			var dataJSONObject = JSONObject.parseObject(result).getJSONObject("data");
			// json的参数一共是3层
			if (dataJSONObject != null) {
				// 课程名称
				var courseName = dataJSONObject.getString("courseName");
				var payloadJSONObject = dataJSONObject.getJSONObject("payload");
				var childrenJSONArray = payloadJSONObject.getJSONArray("children");
				list.addAll(getChildren(childrenJSONArray).stream().map(video -> {
					video.setCoursewareId(payloadJSONObject.getString("coursewareId"));
					video.setCourseName(courseName);
					video.setCourseId(courseID);
					return video;
				}).collect(Collectors.toList()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private List<VideoDTO> getChildren(JSONArray childrenJsonArray) {
		var list = new ArrayList<VideoDTO>();
		if (childrenJsonArray != null && !childrenJsonArray.isEmpty()) {
			for (int i = 0; i < childrenJsonArray.size(); i++) {
				var json = childrenJsonArray.getJSONObject(i);
				if (json.getInteger("nodeType") == 3) {
					list.add(VideoDTO.builder().duration(json.getLong("duration")).fileName(json.getString("name"))
							.id(json.getString("id")).fileId(json.getString("fileId")).build());
				} else {
					list.addAll(getChildren(json.getJSONArray("children")));
				}
			}
		}
		return list;
	}

}
