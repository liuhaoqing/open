package com.liuhq.open.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 练习
 * 
 * @author liuhaoqing
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseDTO implements Serializable {

	private static final long serialVersionUID = 1357790855648502411L;

	/** 练习ID **/
	private Long courseExerciseID;

	/** 课程ID **/
	private String courseID;

	/**  **/
	private String exerciseID;

	/** **/
	private Long maxTimesOfTrying;

	/** **/
	private Long paperLimitingTime;

	/** 试题类型 **/
	private Long exerciseType;

	/** 开始时间 **/
	private Date startDate;

	/** 结束时间 **/
	private Date endDate;

	/** 作业名称 **/
	private String exerciseName;

	/** **/
	private Long paperBuildingMode;

	/** 提交次数 **/
	private Long submitCount;

	/** 最高分数 **/
	private BigDecimal maxScore;

	/**  **/
	private Long isCanDoHomework;

	/** **/
	private String courseWareLocation;

	/** **/
	private String ceshu;

	/** 作业ID **/
	private String studentHomeworkId;

	/** 作业ID **/
	private String homeworkId;

	/** 类型 **/
	private String type;

	/** **/
	private String state;

	/** **/
	private String downloadtoken;

	/** **/
	private String homeCourseId;

}
