package com.liuhq.open.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeaderVo {

	private String name;

	private String value;

}
