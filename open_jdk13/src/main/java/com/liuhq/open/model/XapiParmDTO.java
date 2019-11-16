package com.liuhq.open.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XapiParmDTO {

	/** 用户ID **/
	private String userId;

	/** 用户名 **/
	private String userName;

	/** 用户类型-student **/
	private String userType;

	/** **/
	private String electiveCourseId;

	/** **/
	private String studentStatusId;

	/** **/
	private String organizationId;

	/** **/
	private String signature;

	/** **/
	private String signatureNonce;

	/** **/
	private String timestamp;

	/** **/
	private String appkey;

	/** **/
	private String xapiurl;

	/** **/
	private String platform;

	/** **/
	private String ip;

	/** **/
	private String terminalinfo;

}
