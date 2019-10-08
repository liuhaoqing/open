package com.liuhq.open.util;

import java.util.HashMap;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.collect.Lists;
import com.liuhq.open.constant.OpenConstant;
import com.liuhq.open.model.HeaderVo;
import com.liuhq.open.model.LoginVo;
import com.liuhq.open.model.UniversityInfoVo;
import com.liuhq.open.model.UserVo;
import com.liuhq.open.service.LoginService;

public class LoginUtils {

	private static WebClient client = WebClientFactory.getInstance();

	private static LoginService loginService = new LoginService();

	private static String token;

	private static UniversityInfoVo universityInfo;

	/**
	 * 登录
	 */
	public static UserVo login() {
		return login(OpenConstant.DEFAULT_QR_CODE_PATH);
	}

	/**
	 * 登录
	 */
	public static UserVo login(String filePath) {
		LoginVo loginVo = loginService.getWxImage(filePath);
		try {
			System.out.println("正在登录...");
			WebRequest request = WebRequestFactory.getInstance(RequestUrlEnum.SCAN_CODE_LOGIN,
					Lists.<NameValuePair>newArrayList(new NameValuePair("code", loginVo.getWxCode()),
							new NameValuePair("state", loginVo.getState())));
			// 扫码之后调用的接口
			HtmlPage page = client.getPage(request);
			if (page.getWebResponse().getStatusCode() == HttpStatus.SC_OK) {
				System.out.println("登录成功...");
				return getUserInfo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("登录失败");
	}

	/**
	 * 获取用户信息
	 */
	private static UserVo getUserInfo() throws Exception {
		UnexpectedPage page = client.getPage(WebRequestFactory.getInstance(RequestUrlEnum.GET_USERINFO));
		String result = page.getWebResponse().getContentAsString();
		JSONObject data = JSONObject.parseObject(result).getJSONObject("data");
		return UserVo.builder().realname(data.getString("Realname")).universityStuNo(data.getString("UniversityStuNo"))
				.levelName(data.getString("LevelName")).specialityName(data.getString("SpecialityName"))
				.studentCode(data.getString("StudentCode")).mobilePhone(data.getString("MobilePhone")).build();
	}

	/**
	 * 获取token
	 */
	@SuppressWarnings("serial")
	public static String getToken() {
		if (StringUtils.isBlank(token)) {
			try {
				UnexpectedPage page = client.getPage(WebRequestFactory.getInstance(RequestUrlEnum.SIGN_CONTROLLER, null,
						JSON.toJSONString(new HashMap<String, String>() {
							{
								put("sign", "a89033d60837737f944485229bcc82f1");
								put("timestamp", "1569850730");
							}
						}), HeaderVo.builder().name("Accept").value("application/json, text/plain, */*").build(),
						HeaderVo.builder().name("Content-Type").value("application/json;charset=UTF-8").build(),
						HeaderVo.builder().name("Sec-Fetch-Mode").value("cors").build()));
				String result = page.getWebResponse().getContentAsString();
				token = JSONObject.parseObject(result).getJSONObject("data").getString("token");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return token;
	}

	/**
	 * 获取学校信息
	 */
	public static UniversityInfoVo getUniversityInfo() {
		if (Objects.isNull(universityInfo)) {
			try {
				UnexpectedPage page = client.getPage(WebRequestFactory.getInstance(RequestUrlEnum.GET_UNIVERSITY_CODE));
				String result = page.getWebResponse().getContentAsString();
				JSONObject data = JSONObject.parseObject(result).getJSONObject("data");
				universityInfo = UniversityInfoVo.builder().answerTime(data.getString("AnswerTime"))
						.batchId(data.getInteger("BatchId")).examineeId(data.getString("ExamineeId"))
						.judgeType(data.getString("JudgeType")).levelId(data.getInteger("LevelId"))
						.specialtyId(data.getInteger("SpecialtyId")).universityId(data.getString("UniversityId"))
						.universityCode(data.getString("universityCode")).build();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return universityInfo;
	}

}
