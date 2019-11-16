package com.liuhq.open.model;

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
public class HomeworkDTO {

	private StuHomeWorkInfoDTO stuHomeWorkInfo;
	
	private PaperInfoDTO paperInfo;
	
	/** **/
	private String workAnswerId;
	
	/** **/
	private String judgeInfo;
	
	/** **/
	private String answerInfo;
	
	/** **/
	private Long state;
	
	/** **/
	private HomeWorkInfoDTO homeWorkInfo;
	
}
