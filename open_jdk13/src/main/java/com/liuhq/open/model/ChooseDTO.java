package com.liuhq.open.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 选项
 * @author liuhaoqing
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChooseDTO {

	/** 选项 **/
	private String i1;
	
	/** 选项值 **/
	private String i2;
	
	/** **/
	private Boolean isCorrect;
	
}
