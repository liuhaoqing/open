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
public class HomeWorkInfoDTO {

	/** **/
	private String id;
	
	/** **/
	private String courseId;
	
	/** **/
	private String schoolId;
	
	/** 作业名称 **/
	private String name;
	
	/** **/
	private Long type;
	
	/** 作业开启时间 **/
	private Date beginDate;
	
	/** 作业结束时间 **/
	private Date endDate;
	
	/** **/
	private Double totalScore;
	
	/** **/
	private Long allowableCount;
	
	/** **/
	private Long showAnalysisCount;
	
	/** **/
	private Long topicType;
	
	/** 创建人ID **/
	private String creatorId;
	
	/** 创建时间 **/
	private Date createTime;
	
	/** **/
	private Long state;
	
	/** **/
	private Long timeLimit;
	
	/** **/
	private Long showAnswer;
	
	/** **/
	private Long showAnalysis;
	
	/** **/
	private Long disorder;
	
	/** **/
	private Long passedShowAnswer;
	
	/** **/
	private String appKey;
	
	/** **/
	private Long passScore;
	
	/** **/
	private Long isPerForMance;
	
	/** **/
	private Long count;
	
	/** **/
	private String extend;
	
}
