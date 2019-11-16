package com.liuhq.open.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionDTO {

	private String identifier;
	
	/** 类型名称，如单选题，判断题 **/
	private String title;
	
	/** 1单选题、3判断题 **/
	private Long type;
	
	/** **/
	private Double score;
	
	private List<String> itemIdList;
	
}
