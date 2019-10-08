package com.liuhq.open.util;

import com.gargoylesoftware.htmlunit.HttpMethod;

import lombok.Getter;
import lombok.Setter;

public enum RequestUrlEnum {

	GET_WEI_XIN_CODE("http://learn.open.com.cn/Account/GetWeiXinCode", HttpMethod.POST, "对接微信必要参数"),
	QRCONNECT("https://open.weixin.qq.com/connect/qrconnect", HttpMethod.GET, "获取登录二维码地址及uuid"),
	LOGIN("https://lp.open.weixin.qq.com/connect/l/qrconnect", HttpMethod.GET, "扫码登录"),
	SCAN_CODE_LOGIN("http://learn.open.com.cn/Account/ScanCodeLogin", HttpMethod.GET, "扫码登录"),
	GET_USERINFO("http://learn.open.com.cn/StudentCenter/user/getuserinfo", HttpMethod.GET, "获取用户信息"),
	SIGN_CONTROLLER("https://homeworkapi.open.com.cn/signController", HttpMethod.POST, "获取sign"),
	GET_UNIVERSITY_CODE("http://learn.open.com.cn/StudentCenter/MyWork/GetUniversityCode", HttpMethod.GET, "获取报读学校信息"),
	GET_ONLINE_JSON_ALL("http://learn.open.com.cn/StudentCenter/MyWork/GetOnlineJsonAll", HttpMethod.GET, "获取所有作业"),
	GET_HOMEWORK("https://homeworkapi.open.com.cn/getHomework", HttpMethod.GET, "获取所有题目"),
	GET_SUBMIT_HOMEWORK("https://homeworkapi.open.com.cn/getSubmitHomework", HttpMethod.POST, "提交作业"),
	GET_QUESTION_DETAIL("http://learn.open.com.cn/StudentCenter/OnlineJob/GetQuestionDetail", HttpMethod.GET, "获取答案");

	@Getter
	@Setter
	private String url;

	@Getter
	@Setter
	private HttpMethod method;

	@Getter
	@Setter
	private String desc;

	private RequestUrlEnum(String url, HttpMethod method, String desc) {
		this.url = url;
		this.method = method;
		this.desc = desc;
	}

}
