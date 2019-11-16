package com.liuhq.open.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StuHomeWorkInfoDTO {

	/** **/
	private String studentHomeworkId;
	
	/** 学校ID **/
	private String schoolId;
	
	/** **/
	private String homeworkId;
	
	/** **/
	private String studentId;
	
	/** 提交次数 **/
	private Long submitCount;
	
	/** **/
	private Long allowSubmitCount;
	
	/** 分数 **/
	private Double score;
	
	/** **/
	private Date lastAnswerDate;
	
	/** **/
	private Date lastReadoverDate;
	
	/** **/
	private String maxScoreAnswerId;
	
	/** **/
	private Date createTime;
	
	/** **/
	private String studentCourseId;
	
	/** **/
	private String courseId;
	
	/** **/
	private Long lastAnswerState;
	
	/** **/
	private Long state;
	
	/** **/
	private String rangeKey;
	
	/** **/
	private String appKey;
	
}
