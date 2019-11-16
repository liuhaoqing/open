package com.liuhq.open.util;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.liuhq.open.model.HeaderVo;

public class WebRequestFactory {

	public static WebRequest getInstance(RequestUrlEnum url) {
		return getInstance(url, null, null);
	}

	public static WebRequest getInstance(RequestUrlEnum url, List<NameValuePair> requestParameters) {
		return getInstance(url, requestParameters, null);
	}

	public static WebRequest getInstance(RequestUrlEnum url, String requestBody) {
		return getInstance(url, null, requestBody);
	}

	public static WebRequest getInstance(RequestUrlEnum url, List<NameValuePair> requestParameters, String requestBody,
			HeaderVo... header) {
		try {
			WebRequest request = new WebRequest(new URL(url.getUrl()), url.getMethod());
			if (Objects.nonNull(requestParameters)) {
				request.setRequestParameters(requestParameters);
			}
			if (Objects.nonNull(header)) {
				Arrays.<HeaderVo>asList(header).forEach(vo -> request.setAdditionalHeader(vo.getName(), vo.getValue()));
			}
			if (StringUtils.isNotBlank(requestBody)) {
				request.setRequestBody(requestBody);
			}
			return request;
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Get WebRequest failure...");
	}

}
