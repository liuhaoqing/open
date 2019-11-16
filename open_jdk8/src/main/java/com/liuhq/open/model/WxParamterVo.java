package com.liuhq.open.model;

import lombok.Builder;
import lombok.Data;

/**
 * 微信必要参数
 */
@Data
@Builder
public class WxParamterVo {

	private String id;

	private String appid;

	private String scope;

	private String redirectUri;

	private String state;

	private String style;

	private String href;

}
