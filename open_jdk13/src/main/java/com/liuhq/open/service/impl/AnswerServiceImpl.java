package com.liuhq.open.service.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.liuhq.open.constant.RequestUrlEnum;
import com.liuhq.open.exception.OpenException;
import com.liuhq.open.model.ChooseDTO;
import com.liuhq.open.model.ItemDTO;
import com.liuhq.open.service.AnswerService;
import com.liuhq.open.utils.LoginUtils;
import com.liuhq.open.utils.WebClientFactory;
import com.liuhq.open.utils.WebRequestFactory;

public class AnswerServiceImpl implements AnswerService {

	private WebClient client = WebClientFactory.getInstance();

	@Override
	public ItemDTO getAnswerKey(String itemBankId, String questionId) {
		try {
			var request = WebRequestFactory.getInstance(RequestUrlEnum.GET_QUESTION_DETAIL);
			request.setRequestParameters(
					List.of(new NameValuePair("itemBankId", itemBankId), new NameValuePair("questionId", questionId),
							new NameValuePair("_", String.valueOf(System.currentTimeMillis()))));
			var response = client.loadWebResponse(request);
			var result = response.getContentAsString();
			// 解析json
			var itemJSONObject = JSONObject.parseObject(result).getJSONObject("data");
			var choicesJSONArray = itemJSONObject.getJSONArray("Choices");
			var choices = new ArrayList<ChooseDTO>();
			for (int j = 0; j < choicesJSONArray.size(); j++) {
				var choicesJSONObject = choicesJSONArray.getJSONObject(j);
				choices.add(ChooseDTO.builder().i1(choicesJSONObject.getString("I1"))
						.i2(choicesJSONObject.getString("I2")).build());
			}
			return ItemDTO.builder().i1(itemJSONObject.getString("I1")).i2(itemJSONObject.getString("I2"))
					.i3(itemJSONObject.getLong("I3")).i4(itemJSONObject.getString("I4"))
					.i6(JSONArray.parseArray(JSONArray.toJSONString(itemJSONObject.getJSONArray("I6")), String.class))
					.i7(JSONArray.parseArray(JSONArray.toJSONString(itemJSONObject.getJSONArray("I7")), String.class))
					.i8(JSONArray.parseArray(JSONArray.toJSONString(itemJSONObject.getJSONArray("I8")), String.class))
					.i9(itemJSONObject.getDouble("I9")).i10(itemJSONObject.getString("I10"))
					.i11(itemJSONObject.getLong("I11")).i12(itemJSONObject.getString("I12"))
					.i13(itemJSONObject.getDouble("I13")).i14(itemJSONObject.getString("I14"))
					.i15(JSONArray.parseArray(JSONArray.toJSONString(itemJSONObject.getJSONArray("I15")), Object.class))
					.i16(itemJSONObject.getDouble("I16")).i17(itemJSONObject.getLong("I17"))
					.i18(itemJSONObject.getString("I18")).i19(itemJSONObject.getLong("I19"))
					.i20(itemJSONObject.getLong("I20")).i22(itemJSONObject.getString("I22"))
					.i23(itemJSONObject.getDate("I23")).i24(itemJSONObject.get("I24"))
					.i25(itemJSONObject.getLong("I25")).i26(itemJSONObject.getLong("I26"))
					.i27(itemJSONObject.getLong("I27")).i28(itemJSONObject.getLong("I28"))
					.i29(itemJSONObject.getDate("I29")).i30(itemJSONObject.getString("I30"))
					.i31(itemJSONObject.getLong("I31")).i32(itemJSONObject.getLong("I32"))
					.i33(itemJSONObject.getString("I33")).i34(itemJSONObject.getLong("I34"))
					.i35(itemJSONObject.getLong("I35"))
					.sub(JSONArray.parseArray(JSONArray.toJSONString(itemJSONObject.getJSONArray("Sub")), Object.class))
					.knowledges(JSONArray.parseArray(JSONArray.toJSONString(itemJSONObject.getJSONArray("Knowledges")),
							Object.class))
					.choices(choices).knowledgesJson(itemJSONObject.get("KnowledgesJson")).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenException("获取答案失败");
		}
	}

	@Override
	public boolean stage(String workAnswerId, Map<String, List<Object>> answerMap) {
		try {
			var request = WebRequest.newAboutBlankRequest();
			request.setUrl(new URL(RequestUrlEnum.GET_HOMEWORK_ANSWERS.getUrl() + "?id=" + workAnswerId));
			request.setHttpMethod(RequestUrlEnum.GET_HOMEWORK_ANSWERS.getMethod());
			request.setAdditionalHeaders(
					new HashMap<String, String>(Map.of("Accept", "application/json, text/plain, */*", "appType", "OES",
							"Authorization", "Bearer " + LoginUtils.getUserInfo().getToken(), "Content-Type",
							"application/json", "schoolId", LoginUtils.getUserInfo().getUniversityCode(),
							"Sec-Fetch-Mode", "cors", "Sec-Fetch-Site", "same-site")));
			request.setRequestBody(JSON.toJSONString(answerMap));
			var response = client.loadWebResponse(request);
			// code=200说明成功
			var code = JSONObject.parseObject(response.getContentAsString()).getInteger("code");
			return code == 200;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("serial")
	@Override
	public Double submit(String homeworkAnswerId, List<Object> answerMap) {
		try {
			var request = WebRequest.newAboutBlankRequest();
			request.setUrl(new URL(RequestUrlEnum.GET_SUBMIT_HOMEWORK.getUrl() + "?homeworkAnswerId=" + homeworkAnswerId
					+ "&isDecimal=true&isHalf=true"));
			request.setHttpMethod(RequestUrlEnum.GET_SUBMIT_HOMEWORK.getMethod());
			request.setAdditionalHeaders(
					new HashMap<String, String>(Map.of("Accept", "application/json, text/plain, */*", "appType", "OES",
							"Authorization", "Bearer " + LoginUtils.getUserInfo().getToken(), "Content-Type",
							"application/json", "schoolId", LoginUtils.getUserInfo().getUniversityId())));
			request.setRequestBody(JSON.toJSONString(new HashMap<String, Object>() {
				{
					put("AnswerTime", LoginUtils.getUserInfo().getAnswerTime());
					put("BatchId", LoginUtils.getUserInfo().getBatchId());
					put("ExamineeId", LoginUtils.getUserInfo().getExamineeId());
					put("Items", answerMap);
					put("JudgeType", LoginUtils.getUserInfo().getJudgeType());
					put("LevelId", LoginUtils.getUserInfo().getLevelId());
					put("SpecialtyId", LoginUtils.getUserInfo().getSpecialtyId());
					put("UniversityId", LoginUtils.getUserInfo().getUniversityId());
					put("isDecimal", true);
					put("isErrorAnswer", true);
					put("isHalf", true);
				}
			}));
			var response = client.loadWebResponse(request);
			var score = JSONObject.parseObject(response.getContentAsString()).getJSONObject("data").getDouble("score");
			return score;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
