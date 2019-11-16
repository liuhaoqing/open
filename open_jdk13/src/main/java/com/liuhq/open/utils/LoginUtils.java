package com.liuhq.open.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liuhq.open.constant.RequestUrlEnum;
import com.liuhq.open.exception.OpenException;
import com.liuhq.open.model.LoginInfoDTO;

/**
 * 登录工具类
 * 
 * @author liuhaoqing
 *
 */
public class LoginUtils {

	private static LoginInfoDTO loginInfo;

	/**
	 * 获取用户登录信息
	 * 
	 * @return 用户登录信息
	 */
	public synchronized static LoginInfoDTO getUserInfo() {
		if (Objects.isNull(loginInfo)) {
			// 获取基本信息
			var basicJSONObject = getBasicInfo();
			// 获取就读信息
			var schoolJSONObject = getSchoolInfo();
			// 获取token
			String token = getToken();
			loginInfo = LoginInfoDTO.builder().realname(basicJSONObject.getString("Realname"))
					.universityStuNo(basicJSONObject.getString("UniversityStuNo"))
					.totaPoints(basicJSONObject.getInteger("TotaPoints"))
					.studentStyleStr(basicJSONObject.getString("studentStyleStr"))
					.levelName(basicJSONObject.getString("LevelName"))
					.specialityName(basicJSONObject.getString("SpecialityName")).url(basicJSONObject.getString("Url"))
					.isYearRegInfo(basicJSONObject.getBoolean("IsYearRegInfo"))
					.isBind(basicJSONObject.getBoolean("IsBind")).studentCode(basicJSONObject.getString("StudentCode"))
					.mobilePhone(basicJSONObject.getString("MobilePhone"))
					.isLogin(basicJSONObject.getBoolean("IsLogin")).sex(basicJSONObject.getString("Sex"))
					.universityCode(schoolJSONObject.getString("universityCode"))
					.examineeId(schoolJSONObject.getString("ExamineeId"))
					.answerTime(schoolJSONObject.getDate("AnswerTime"))
					.universityId(schoolJSONObject.getString("UniversityId"))
					.batchId(schoolJSONObject.getLong("BatchId")).levelId(schoolJSONObject.getLong("LevelId"))
					.specialtyId(schoolJSONObject.getLong("SpecialtyId"))
					.judgeType(schoolJSONObject.getString("JudgeType")).token(token).build();
		}
		return loginInfo;
	}

	/**
	 * 获取个人基本信息失败
	 * 
	 * @return 个人基本信息
	 */
	private static JSONObject getBasicInfo() {
		try {
			var response = WebClientFactory.getInstance()
					.loadWebResponse(WebRequestFactory.getInstance(RequestUrlEnum.GET_USERINFO));
			var result = response.getContentAsString();
			return JSONObject.parseObject(result).getJSONObject("data");
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenException("获取个人基本信息失败");
		}
	}

	/**
	 * 获取就读信息失败
	 * 
	 * @return 就读信息
	 */
	private static JSONObject getSchoolInfo() {
		try {
			var response = WebClientFactory.getInstance()
					.loadWebResponse(WebRequestFactory.getInstance(RequestUrlEnum.GET_UNIVERSITY_CODE));
			var result = response.getContentAsString();
			return JSONObject.parseObject(result).getJSONObject("data");
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenException("获取就读信息失败");
		}
	}

	/**
	 * 获取token
	 */
	private static String getToken() {
		try {
			var request = WebRequestFactory.getInstance(RequestUrlEnum.SIGN_CONTROLLER);
			request.setRequestBody(
					JSON.toJSONString(Map.of("sign", "a89033d60837737f944485229bcc82f1", "timestamp", "1569850730")));
			request.setAdditionalHeaders(
					new HashMap<String, String>(Map.of("Accept", "application/json, text/plain, */*", "Content-Type",
							"application/json;charset=UTF-8", "Sec-Fetch-Mode", "cors")));
			var response = WebClientFactory.getInstance().loadWebResponse(request);
			var result = response.getContentAsString();
			return JSONObject.parseObject(result).getJSONObject("data").getString("token");
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenException("获取token失败");
		}
	}

}
