package com.liuhq.open.constant;

import com.gargoylesoftware.htmlunit.HttpMethod;

import lombok.Getter;
import lombok.Setter;

public enum RequestUrlEnum {

	GET_WEI_XIN_CODE("http://learn.open.com.cn/Account/GetWeiXinCode", HttpMethod.POST),
	QR_CONNECT("https://open.weixin.qq.com/connect/qrconnect", HttpMethod.GET),
	LOGIN("https://lp.open.weixin.qq.com/connect/l/qrconnect", HttpMethod.GET),
	SCAN_CODE_LOGIN("http://learn.open.com.cn/Account/ScanCodeLogin", HttpMethod.GET),
	GET_USERINFO("http://learn.open.com.cn/StudentCenter/user/getuserinfo", HttpMethod.GET),
	GET_ONLINE_JSON_ALL("http://learn.open.com.cn/StudentCenter/MyWork/GetOnlineJsonAll", HttpMethod.GET),
	GET_HOMEWORK("https://homeworkapi.open.com.cn/getHomework", HttpMethod.GET),
	GET_UNIVERSITY_CODE("http://learn.open.com.cn/StudentCenter/MyWork/GetUniversityCode", HttpMethod.GET),
	SIGN_CONTROLLER("https://homeworkapi.open.com.cn/signController", HttpMethod.POST),
	GET_QUESTION_DETAIL("http://learn.open.com.cn/StudentCenter/OnlineJob/GetQuestionDetail", HttpMethod.GET),
	GET_HOMEWORK_ANSWERS("https://homeworkapi.open.com.cn/getHomeworkAnswers", HttpMethod.POST),
	GET_SUBMIT_HOMEWORK("https://homeworkapi.open.com.cn/getSubmitHomework", HttpMethod.POST),
	GET_MY_COURSE("http://learn.open.com.cn/StudentCenter/MyCourse/GetMyCourse", HttpMethod.GET),
	GET_VIDEO_LEARN_PROGRESS("http://learn.open.com.cn/StudentCenter/CourseWare/GetVideoLearnProgress", HttpMethod.GET),
	GET_COURSE_WARE_JSON_BY_RS("http://learn.open.com.cn/StudentCenter/MyCourse/GetCourseWareJsonByRs", HttpMethod.GET),
	DATA_COLLECTION("https://xapi.open.com.cn/api/v1/students/data-collection", HttpMethod.POST),
	GET_XAPI_PARM("http://learn.open.com.cn/StudentCenter/courseware/GetXapiParm", HttpMethod.GET),
	SAVE_VIDEO_PROGRESS("http://learn.open.com.cn/StudentCenter/CourseWare/SaveVideoProgress", HttpMethod.POST);

	@Getter
	@Setter
	private String url;

	@Getter
	@Setter
	private HttpMethod method;

	RequestUrlEnum(String url, HttpMethod method) {
		this.url = url;
		this.method = method;
	}

}
