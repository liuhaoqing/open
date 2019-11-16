package com.liuhq.open.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerVo {

	private String answerId;

	/** 題目 **/
	private String question;
	
	private String workAnswerId;
	
	private String itemBankId;

	private List<ChooseVo> chooseList;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer().append("题目ID：").append(answerId).append("\n").append("题目：")
				.append(question).append("\n");
		if (chooseList != null) {
			chooseList
					.forEach(choose -> sb.append(choose.getOption()).append(":").append(choose.getKey()).append("\t"));
		}
		return new String(sb);
	}

}
