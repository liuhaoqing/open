package com.liuhq.open.util;

import com.gargoylesoftware.htmlunit.HttpMethod;

import lombok.Getter;
import lombok.Setter;

public enum RequestUrlEnum {

	GET_WEI_XIN_CODE("http://learn.open.com.cn/Account/GetWeiXinCode", HttpMethod.POST),
	QRCONNECT("https://open.weixin.qq.com/connect/qrconnect",HttpMethod.GET),
	SCAN_CODE_LOGIN("http://learn.open.com.cn/Account/ScanCodeLogin",HttpMethod.GET),
	GET_ONLINE_JSON_ALL("http://learn.open.com.cn/StudentCenter/MyWork/GetOnlineJsonAll",HttpMethod.GET),
	GET_UNIVERSITY_CODE("http://learn.open.com.cn/StudentCenter/MyWork/GetUniversityCode",HttpMethod.GET),
	GET_HOMEWORK("https://homeworkapi.open.com.cn/getHomework",HttpMethod.GET),
	SIGN_CONTROLLER("https://homeworkapi.open.com.cn/signController", HttpMethod.POST),
	GET_HOMEWORK_ANSWERS("https://homeworkapi.open.com.cn/getHomeworkAnswers",HttpMethod.POST),
	GET_SUBMIT_HOMEWORK("https://homeworkapi.open.com.cn/getSubmitHomework",HttpMethod.POST),
	GET_QUESTION_DETAIL("http://learn.open.com.cn/StudentCenter/OnlineJob/GetQuestionDetail",HttpMethod.GET);

	@Getter
	@Setter
	private String url;

	@Getter
	@Setter
	private HttpMethod method;

	private RequestUrlEnum(String url, HttpMethod method) {
		this.url = url;
		this.method = method;
	}

}
