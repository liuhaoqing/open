package com.liuhq.open.service;

import com.liuhq.open.model.HomeworkDTO;

/**
 * 作业service接口
 * 
 * @author liuhaoqing
 *
 */
public interface HomeworkService {

	/**
	 * 根据作业ID获取作业试题明细
	 * 
	 * @param studentHomeworkId 作业ID
	 */
	HomeworkDTO getByStudentHomeworkId(String studentHomeworkId);

}
