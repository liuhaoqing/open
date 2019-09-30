package com.liuhq.open.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
