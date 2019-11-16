package com.liuhq.open.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 微信二维码扫码登录必要参数
 * 
 * @author liuhaoqing
 *
 */
@Data
@Builder
public class WeiXinCodeDTO implements Serializable {

	private static final long serialVersionUID = -8909310311668270294L;

	private String id;

	private String appid;

	private String scope;

	private String redirectUri;

	private String state;

	private String style;

	private String href;

}
