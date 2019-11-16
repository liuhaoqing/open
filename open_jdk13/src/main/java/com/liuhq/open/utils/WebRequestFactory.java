package com.liuhq.open.utils;

import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.liuhq.open.constant.RequestUrlEnum;
import com.liuhq.open.exception.OpenException;

public class WebRequestFactory {

	public static WebRequest getInstance(RequestUrlEnum requestUrl) {
		try {
			var request = WebRequest.newAboutBlankRequest();
			request.setUrl(new URL(requestUrl.getUrl()));
			request.setHttpMethod(requestUrl.getMethod());
			return request;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		throw new OpenException("获取request失败");
	}

}
