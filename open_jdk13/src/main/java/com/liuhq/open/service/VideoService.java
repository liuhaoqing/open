package com.liuhq.open.service;

import com.liuhq.open.model.XapiParmDTO;

public interface VideoService {

	XapiParmDTO getParamInfo(Long courseId);
	
	void dataCollection(String jsonData,String appKey,String signature,String signatureNonce,String timestamp);
	
}
