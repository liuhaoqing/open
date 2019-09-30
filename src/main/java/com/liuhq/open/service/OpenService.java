package com.liuhq.open.service;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.liuhq.open.model.AnswerVo;
import com.liuhq.open.model.ChooseVo;
import com.liuhq.open.model.CourseVo;
import com.liuhq.open.util.LoginUtils;
import com.liuhq.open.util.RequestUrlEnum;
import com.liuhq.open.util.WebClientFactory;

public class OpenService {

	private WebClient client = WebClientFactory.getInstance();

	@SuppressWarnings("serial")
	public void autoAnswer() {
		// 剩余未完成作业
		List<CourseVo> courseList = getOnlineJsonAll().stream().filter(course -> {
			return StringUtils.isNotBlank(course.getStudentHomeworkId());
		}).collect(Collectors.<CourseVo>toList());
		courseList.forEach(course -> {
			System.out.println(course);
			List<AnswerVo> answerList = getHomework(course.getStudentHomeworkId());
			Map<String, List<Object>> answerQuestionMap = Maps.<String, List<Object>>newHashMap();
			List<Object> answerQuestionList = Lists.<Object>newArrayList();
			answerList.forEach(answer -> {
				System.out.println(answer);
				System.out.println("--------------");
				answerQuestionList.add(new HashMap<String, Object>() {
					{
						put("I1", answer.getAnswerId());
						put("I15", getAnswerKey(answer.getItemBankId(), answer.getAnswerId()));
						put("Sub", Arrays.asList());
					}
				});
			});
			answerQuestionMap.put("Items", answerQuestionList);
			Double score = submit(answerList.get(0).getWorkAnswerId(), new HashMap<String, Object>() {
				{
					put("AnswerTime", LoginUtils.getUniversityInfo().getAnswerTime());
					put("BatchId", LoginUtils.getUniversityInfo().getBatchId());
					put("ExamineeId", LoginUtils.getUniversityInfo().getExamineeId());
					put("Items", answerQuestionList);
					put("JudgeType", LoginUtils.getUniversityInfo().getJudgeType());
					put("LevelId", LoginUtils.getUniversityInfo().getLevelId());
					put("SpecialtyId", LoginUtils.getUniversityInfo().getSpecialtyId());
					put("UniversityId", LoginUtils.getUniversityInfo().getUniversityId());
					put("isDecimal", true);
					put("isErrorAnswer", true);
					put("isHalf", true);
				}
			});
			System.out.println("课程：" + course.getCourseName());
			System.out.println("作业：" + course.getExerciseName());
			System.out.println("得分：" + score);
			System.out.println("#######################################");
		});
	}

//	
	public List<CourseVo> getOnlineJsonAll() {
		List<CourseVo> list = Lists.<CourseVo>newArrayList();
		try {
			WebRequest request = new WebRequest(new URL(RequestUrlEnum.GET_ONLINE_JSON_ALL.getUrl()),
					RequestUrlEnum.GET_ONLINE_JSON_ALL.getMethod());
			UnexpectedPage page = client.getPage(request);
			String result = page.getWebResponse().getContentAsString();
			JSONArray dataJSONArray = JSONObject.parseObject(result).getJSONArray("data");
			for (int i = 0; i < dataJSONArray.size(); i++) {
				JSONObject data = dataJSONArray.getJSONObject(i);
				String courseName = data.getString("CourseName");
				JSONArray detailJSONArray = data.getJSONArray("Data");
				for (int j = 0; j < detailJSONArray.size(); j++) {
					JSONObject detail = detailJSONArray.getJSONObject(j);
					String exerciseName = detail.getString("ExerciseName");
					String studentHomeworkId = detail.getString("studentHomeworkId");
					list.add(CourseVo.builder().courseName(courseName).exerciseName(exerciseName)
							.studentHomeworkId(studentHomeworkId).build());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("serial")
	public List<AnswerVo> getHomework(String studentHomeworkId) {
		List<AnswerVo> answerList = Lists.<AnswerVo>newArrayList();
		try {
			WebRequest request = new WebRequest(new URL(RequestUrlEnum.GET_HOMEWORK.getUrl()),
					RequestUrlEnum.GET_HOMEWORK.getMethod());
			request.setRequestParameters(
					Lists.<NameValuePair>newArrayList(new NameValuePair("studentHomeworkId", studentHomeworkId)));
			request.setAdditionalHeaders(new HashMap<String, String>() {
				{
					put("Accept", "application/json, text/plain, */*");
					put("appType", "OES");
					put("schoolId", LoginUtils.getUniversityInfo().getUniversityId());
					put("Sec-Fetch-Mode", "cors");
					put("Authorization", "Bearer " + LoginUtils.getToken());
				}
			});
			Page page = client.getPage(request);
			String result = page.getWebResponse().getContentAsString();
			JSONObject data = JSONObject.parseObject(result).getJSONObject("data");
			JSONArray items = data.getJSONObject("paperInfo").getJSONArray("Items");
			for (int i = 0; i < items.size(); i++) {
				JSONObject item = items.getJSONObject(i);
				JSONArray choices = item.getJSONArray("Choices");
				List<ChooseVo> chooseList = Lists.<ChooseVo>newArrayList();
				for (int j = 0; j < choices.size(); j++) {
					JSONObject choose = choices.getJSONObject(j);
					chooseList.add(ChooseVo.builder().option(choose.getString("I1")).key(choose.getString("I2"))
							.index(String.valueOf(j)).build());
				}
				answerList.add(AnswerVo.builder()
						.itemBankId(
								data.getJSONObject("paperInfo").getJSONArray("Items").getJSONObject(0).getString("I4"))
						.workAnswerId(data.getString("workAnswerId")).answerId(item.getString("I1"))
						.question(item.getString("I2")).chooseList(chooseList).build());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return answerList;
	}

	private void answerQuestion(String workAnswerId, Map<String, List<Object>> answerMap) {
		try {
			WebRequest request = new WebRequest(
					new URL(RequestUrlEnum.GET_HOMEWORK_ANSWERS.getUrl() + "?id=" + workAnswerId),
					RequestUrlEnum.GET_HOMEWORK_ANSWERS.getMethod());
//			request.setRequestParameters(Lists.<NameValuePair>newArrayList(
//				new NameValuePair("id", workAnswerId)
//					));
			request.setRequestBody(JSON.toJSONString(answerMap));
			request.setAdditionalHeaders(new HashMap<String, String>() {
				{
					put("Accept", "application/json, text/plain, */*");
					put("appType", "OES");
					put("Authorization", "Bearer " + LoginUtils.getToken());
					put("Content-Type", "application/json");
					put("schoolId", LoginUtils.getUniversityInfo().getUniversityId());
				}
			});
			Page page = client.getPage(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("serial")
	private Double submit(String homeworkAnswerId, Map<String, Object> answerMap) {
		try {
			WebRequest request = new WebRequest(new URL(RequestUrlEnum.GET_SUBMIT_HOMEWORK.getUrl()
					+ "?homeworkAnswerId=" + homeworkAnswerId + "&isDecimal=true&isHalf=true"),
					RequestUrlEnum.GET_SUBMIT_HOMEWORK.getMethod());
			request.setRequestBody(JSON.toJSONString(answerMap));
			request.setAdditionalHeaders(new HashMap<String, String>() {
				{
					put("Accept", "application/json, text/plain, */*");
					put("appType", "OES");
					put("Authorization", "Bearer " + LoginUtils.getToken());
					put("Content-Type", "application/json");
					put("schoolId", LoginUtils.getUniversityInfo().getUniversityId());
				}
			});
			Page page = client.getPage(request);
			String result = page.getWebResponse().getContentAsString();
			return JSONObject.parseObject(result).getJSONObject("data").getDouble("score");
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("获取得分失败");
	}

	public List<String> getAnswerKey(String itemBankId, String questionId) {
		try {
			WebRequest request = new WebRequest(
					new URL(RequestUrlEnum.GET_QUESTION_DETAIL.getUrl() + "?itemBankId=" + itemBankId + "&questionId="
							+ questionId + "&_=" + System.currentTimeMillis()),
					RequestUrlEnum.GET_QUESTION_DETAIL.getMethod());
			Page page = client.getPage(request);
			String result = page.getWebResponse().getContentAsString();
			return JSONObject.parseObject(result).getJSONObject("data").getJSONArray("I7").toJavaList(String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("获取答案失败");
	}

}
