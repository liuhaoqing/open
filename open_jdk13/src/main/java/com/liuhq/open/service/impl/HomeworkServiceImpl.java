package com.liuhq.open.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.liuhq.open.constant.RequestUrlEnum;
import com.liuhq.open.exception.OpenException;
import com.liuhq.open.model.ChooseDTO;
import com.liuhq.open.model.HomeWorkInfoDTO;
import com.liuhq.open.model.HomeworkDTO;
import com.liuhq.open.model.ItemDTO;
import com.liuhq.open.model.ModelDTO;
import com.liuhq.open.model.PaperInfoDTO;
import com.liuhq.open.model.SectionDTO;
import com.liuhq.open.model.StuHomeWorkInfoDTO;
import com.liuhq.open.service.HomeworkService;
import com.liuhq.open.utils.LoginUtils;
import com.liuhq.open.utils.WebClientFactory;
import com.liuhq.open.utils.WebRequestFactory;

/**
 * 作业service接口实现
 * 
 * @author liuhaoqing
 *
 */
public class HomeworkServiceImpl implements HomeworkService {

	private WebClient client = WebClientFactory.getInstance();

	/**
	 * 根据作业ID获取作业试题明细
	 * 
	 * @param studentHomeworkId 作业ID
	 */
	@Override
	public HomeworkDTO getByStudentHomeworkId(String studentHomeworkId) {
		try {
			var request = WebRequestFactory.getInstance(RequestUrlEnum.GET_HOMEWORK);
			request.setRequestParameters(List.of(new NameValuePair("studentHomeworkId", studentHomeworkId)));
			request.setAdditionalHeaders(
					new HashMap<String, String>(Map.of("Accept", "application/json, text/plain, */*", "appType", "OES",
							"schoolId", LoginUtils.getUserInfo().getUniversityId(), "Sec-Fetch-Mode", "cors",
							"Authorization", "Bearer " + LoginUtils.getUserInfo().getToken())));
			var response = client.loadWebResponse(request);
			var result = response.getContentAsString();
			// 解析数据
			var dataJSONObject = JSONObject.parseObject(result).getJSONObject("data");

			var stuHomeWorkInfoJSONObject = dataJSONObject.getJSONObject("stuHomeWorkInfo");
			var stuHomeWorkInfo = StuHomeWorkInfoDTO.builder()
					.studentHomeworkId(stuHomeWorkInfoJSONObject.getString("studentHomeworkId"))
					.schoolId(stuHomeWorkInfoJSONObject.getString("schoolId"))
					.homeworkId(stuHomeWorkInfoJSONObject.getString("homeworkId"))
					.studentId(stuHomeWorkInfoJSONObject.getString("studentId"))
					.submitCount(stuHomeWorkInfoJSONObject.getLong("submitCount"))
					.allowSubmitCount(stuHomeWorkInfoJSONObject.getLong("allowSubmitCount"))
					.score(stuHomeWorkInfoJSONObject.getDouble("score"))
					.lastAnswerDate(stuHomeWorkInfoJSONObject.getDate("lastAnswerDate"))
					.lastReadoverDate(stuHomeWorkInfoJSONObject.getDate("lastReadoverDate"))
					.maxScoreAnswerId(stuHomeWorkInfoJSONObject.getString("maxScoreAnswerId"))
					.createTime(stuHomeWorkInfoJSONObject.getDate("createTime"))
					.studentCourseId(stuHomeWorkInfoJSONObject.getString("studentCourseId"))
					.courseId(stuHomeWorkInfoJSONObject.getString("courseId"))
					.lastAnswerState(stuHomeWorkInfoJSONObject.getLong("lastAnswerState"))
					.state(stuHomeWorkInfoJSONObject.getLong("state"))
					.rangeKey(stuHomeWorkInfoJSONObject.getString("rangeKey"))
					.appKey(stuHomeWorkInfoJSONObject.getString("appKey")).build();

			var paperInfoJSONObject = dataJSONObject.getJSONObject("paperInfo");

			var modelJSONObject = paperInfoJSONObject.getJSONObject("Model");
			var model = ModelDTO.builder().p1(modelJSONObject.getString("P1")).p2(modelJSONObject.getString("P2"))
					.p3(modelJSONObject.getString("P3")).p4(modelJSONObject.getString("P4"))
					.p5(modelJSONObject.getLong("P5")).p6(modelJSONObject.getString("P6")).build();

			var items = new ArrayList<ItemDTO>();
			var itemsJSONArray = paperInfoJSONObject.getJSONArray("Items");
			for (int i = 0; i < itemsJSONArray.size(); i++) {
				var itemJSONObject = itemsJSONArray.getJSONObject(i);
				var choicesJSONArray = itemJSONObject.getJSONArray("Choices");
				var choices = new ArrayList<ChooseDTO>();
				for (int j = 0; j < choicesJSONArray.size(); j++) {
					var choicesJSONObject = choicesJSONArray.getJSONObject(j);
					choices.add(ChooseDTO.builder().i1(choicesJSONObject.getString("I1"))
							.i2(choicesJSONObject.getString("I2")).build());
				}
				items.add(ItemDTO.builder().i1(itemJSONObject.getString("I1")).i2(itemJSONObject.getString("I2"))
						.i3(itemJSONObject.getLong("I3")).i4(itemJSONObject.getString("I4"))
						.i6(JSONArray.parseArray(JSONArray.toJSONString(itemJSONObject.getJSONArray("I6")),
								String.class))
						.i8(JSONArray.parseArray(JSONArray.toJSONString(itemJSONObject.getJSONArray("I8")),
								String.class))
						.i9(itemJSONObject.getDouble("I9")).i10(itemJSONObject.getString("I10"))
						.i11(itemJSONObject.getLong("I11")).i12(itemJSONObject.getString("I12"))
						.i13(itemJSONObject.getDouble("I13")).i14(itemJSONObject.getString("I14"))
						.i15(JSONArray.parseArray(JSONArray.toJSONString(itemJSONObject.getJSONArray("I15")),
								Object.class))
						.i16(itemJSONObject.getDouble("I16")).i17(itemJSONObject.getLong("I17"))
						.i18(itemJSONObject.getString("I18")).i19(itemJSONObject.getLong("I19"))
						.i20(itemJSONObject.getLong("I20")).i22(itemJSONObject.getString("I22"))
						.i23(itemJSONObject.getDate("I23")).i25(itemJSONObject.getLong("I25"))
						.i26(itemJSONObject.getLong("I26")).i27(itemJSONObject.getLong("I27"))
						.i28(itemJSONObject.getLong("I28")).i29(itemJSONObject.getDate("I29"))
						.i30(itemJSONObject.getString("I30")).i31(itemJSONObject.getLong("I31"))
						.i32(itemJSONObject.getLong("I32")).i33(itemJSONObject.getString("I33"))
						.i34(itemJSONObject.getLong("I34")).i35(itemJSONObject.getLong("I35")).sub(JSONArray
								.parseArray(JSONArray.toJSONString(itemJSONObject.getJSONArray("Sub")), Object.class))
						.choices(choices).build());
			}

			var sections = new ArrayList<SectionDTO>();
			var sectionsJSONArray = paperInfoJSONObject.getJSONArray("Sections");
			for (int i = 0; i < sectionsJSONArray.size(); i++) {
				var sectionsJSONObject = sectionsJSONArray.getJSONObject(i);
				sections.add(SectionDTO.builder().identifier(sectionsJSONObject.getString("Identifier"))
						.title(sectionsJSONObject.getString("Title")).type(sectionsJSONObject.getLong("Type"))
						.score(sectionsJSONObject.getDouble("Score"))
						.itemIdList(JSONArray.parseArray(
								JSONArray.toJSONString(sectionsJSONObject.getJSONArray("ItemID")), String.class))
						.build());
			}

			var paperInfo = PaperInfoDTO.builder().model(model).items(items).sections(sections).build();

			var homeWorkInfoJSONObject = dataJSONObject.getJSONObject("homeWorkInfo");
			var homeWorkInfo = HomeWorkInfoDTO.builder().id(homeWorkInfoJSONObject.getString("id"))
					.courseId(homeWorkInfoJSONObject.getString("courseId"))
					.schoolId(homeWorkInfoJSONObject.getString("schoolId"))
					.name(homeWorkInfoJSONObject.getString("name")).type(homeWorkInfoJSONObject.getLong("type"))
					.beginDate(homeWorkInfoJSONObject.getDate("beginDate"))
					.endDate(homeWorkInfoJSONObject.getDate("endDate"))
					.totalScore(homeWorkInfoJSONObject.getDouble("totalScore"))
					.allowableCount(homeWorkInfoJSONObject.getLong("allowableCount"))
					.showAnalysisCount(homeWorkInfoJSONObject.getLong("showAnalysisCount"))
					.topicType(homeWorkInfoJSONObject.getLong("topicType"))
					.creatorId(homeWorkInfoJSONObject.getString("creatorId"))
					.createTime(homeWorkInfoJSONObject.getDate("createTime"))
					.state(homeWorkInfoJSONObject.getLong("state"))
					.timeLimit(homeWorkInfoJSONObject.getLong("timeLimit"))
					.showAnswer(homeWorkInfoJSONObject.getLong("showAnswer"))
					.showAnalysis(homeWorkInfoJSONObject.getLong("showAnalysis"))
					.disorder(homeWorkInfoJSONObject.getLong("disorder"))
					.passedShowAnswer(homeWorkInfoJSONObject.getLong("passedShowAnswer"))
					.appKey(homeWorkInfoJSONObject.getString("appKey"))
					.passScore(homeWorkInfoJSONObject.getLong("passScore"))
					.isPerForMance(homeWorkInfoJSONObject.getLong("homeWorkInfoJSONObject"))
					.count(homeWorkInfoJSONObject.getLong("count")).extend(homeWorkInfoJSONObject.getString("extend"))
					.build();
			return HomeworkDTO.builder().stuHomeWorkInfo(stuHomeWorkInfo).paperInfo(paperInfo)
					.workAnswerId(dataJSONObject.getString("workAnswerId"))
					.judgeInfo(dataJSONObject.getString("judgeInfo")).answerInfo(dataJSONObject.getString("answerInfo"))
					.state(dataJSONObject.getLong("state")).homeWorkInfo(homeWorkInfo).build();
		} catch (IOException e) {
			e.printStackTrace();
			throw new OpenException("获取作业失败");
		}
	}

}
