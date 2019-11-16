package com.liuhq.open.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 作业
 * 
 * @author liuhaoqing
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkDTO implements Serializable {

	private static final long serialVersionUID = -138872229897983788L;

	/** 课程名称 **/
	private String courseName;
	
	/** 练习列表 **/
	private List<ExerciseDTO> exerciseList;
	
}
