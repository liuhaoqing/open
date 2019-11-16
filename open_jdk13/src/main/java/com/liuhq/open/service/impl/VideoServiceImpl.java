package com.liuhq.open.service.impl;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.liuhq.open.constant.RequestUrlEnum;
import com.liuhq.open.exception.OpenException;
import com.liuhq.open.model.XapiParmDTO;
import com.liuhq.open.service.VideoService;
import com.liuhq.open.utils.WebClientFactory;

public class VideoServiceImpl implements VideoService {

	private WebClient client = WebClientFactory.getInstance();

	@Override
	public XapiParmDTO getParamInfo(Long courseId) {
		try {
			var request = WebRequest.newAboutBlankRequest();
			request.setUrl(new URL(RequestUrlEnum.GET_XAPI_PARM.getUrl()));
			request.setHttpMethod(RequestUrlEnum.GET_XAPI_PARM.getMethod());
			request.setRequestParameters(List.of(new NameValuePair("courseid", String.valueOf(courseId))));
			var response = client.loadWebResponse(request);
			return JSONObject.parseObject(response.getContentAsString()).getObject("data", XapiParmDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new OpenException("获取sign失败");
	}

	@Override
	public void dataCollection(String jsonData, String appKey, String signature, String signatureNonce,
			String timestamp) {
		try {
			var request = WebRequest.newAboutBlankRequest();
			request.setUrl(new URL(RequestUrlEnum.DATA_COLLECTION.getUrl() + "?jsonData="
					+ URLEncoder.encode(jsonData, StandardCharsets.UTF_8)));
			request.setHttpMethod(RequestUrlEnum.DATA_COLLECTION.getMethod());
			request.setAdditionalHeaders(new HashMap<String, String>(Map.of("Accept",
					"application/json, text/plain, */*", "appKey", appKey, "platform", "1", "Sec-Fetch-Mode", "cors",
					"signature", signature, "signatureNonce", signatureNonce, "timestamp", timestamp)));
			client.loadWebResponse(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
