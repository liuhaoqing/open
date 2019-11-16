package com.liuhq.open.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 观看视频进程
 * 
 * @author liuhaoqing
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoLearnProgressDTO implements Serializable {

	private static final long serialVersionUID = 1217030291519699261L;

	/** 课程ID **/
	private Long courseID;

	/**  **/
	private String resourceID;

	/** **/
	private Long data;

	/** type=2:观看未完成，type=3已观看完成 **/
	private Long type;

	/** **/
	private Date createDate;

	/** **/
	private Long totalTimes;

}
