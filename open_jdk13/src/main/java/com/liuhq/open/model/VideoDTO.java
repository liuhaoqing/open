package com.liuhq.open.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视频
 * 
 * @author liuhaoqing
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDTO implements Serializable {

	private static final long serialVersionUID = 8825064358609940297L;

	/** 视频时长，单位毫秒 **/
	private Long duration;
	
	private String coursewareId;
	
	/** 课程名称 **/
	private String courseName;

	/** 课程ID **/
	private Long courseId;

	/** 视频ID **/
	private String id;
	
	private String fileId;
	
	/** **/
	private String fileName;

}
