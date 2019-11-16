package com.liuhq.open.service;

import java.util.List;
import java.util.Map;

import com.liuhq.open.model.ItemDTO;

/**
 * 回答作业service
 * 
 * @author liuhaoqing
 *
 */
public interface AnswerService {

	/**
	 * 获取答案
	 * @param itemBankId
	 * @param questionId
	 * @return
	 */
	ItemDTO getAnswerKey(String itemBankId,String questionId);
	
	/**
	 * 暂存
	 * @param workAnswerId
	 * @return
	 */
	boolean stage(String workAnswerId,Map<String, List<Object>> answerMap);
	
	/**
	 * 提交
	 * @param workAnswerId
	 * @param answerMap
	 * @return
	 */
	Double submit(String homeworkAnswerId,List<Object> answerMap);
	
}
