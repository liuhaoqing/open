package com.liuhq.open.service;

import java.util.List;

import com.liuhq.open.model.CourseDTO;
import com.liuhq.open.model.VideoDTO;
import com.liuhq.open.model.VideoLearnProgressDTO;

public interface CourseService {

	/** 获取所有 **/
	List<CourseDTO> getAll();
	
	/** 获取所有已学习过的课程 **/
	List<VideoLearnProgressDTO> listVideoLearnProgress(Long courseID);
	
	/** 获取所有视频 **/
	List<VideoDTO> listAllVideo(Long courseID);
	
}
