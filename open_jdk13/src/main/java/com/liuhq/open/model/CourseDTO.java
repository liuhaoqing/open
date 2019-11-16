package com.liuhq.open.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程实体
 * 
 * @author liuhaoqing
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO implements Serializable {

	private static final long serialVersionUID = 6002585082414825460L;

	/** 课程名称 **/
	private String courseName;

	/** code **/
	private String courseCode;

	/** 封面图片地址 **/
	private String courseImg;

	/** **/
	private Long courseWareCount;

	/** **/
	private Boolean isTrue;

	/** **/
	private BigDecimal progress;

	/** **/
	private Boolean isRedirect;

	/** **/
	private String homeWorkUrl;

	/** **/
	private Long studentCourseID;

	/** 课程ID **/
	private Long courseID;

	/** **/
	private Date choosedate;

	/** 状态名称 **/
	private String courseStatus;

	/** **/
	private Long teachPlanID;

	/** **/
	private String statusCode;

	/** **/
	private Long creditHour;

	/** **/
	private String studyCourseModeName;

	/** **/
	private String studentCode;

	/** **/
	private Long bookingExamNo;

	/** **/
	private Date beginStudyTime;

	/** **/
	private Long validPeriod;

	/** **/
	private Long term;

}
