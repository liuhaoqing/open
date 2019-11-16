package com.liuhq.open.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityInfoVo {

	private String answerTime;

	private Integer batchId;

	private String examineeId;

	private String judgeType;

	private Integer levelId;

	private Integer specialtyId;

	private String universityId;

	private String universityCode;

}
