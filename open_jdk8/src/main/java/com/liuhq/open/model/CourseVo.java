package com.liuhq.open.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseVo {

	private String courseName;

	private String exerciseName;

	private String studentHomeworkId;

	@Override
	public String toString() {
		return new String(new StringBuffer().append("作业ID：").append(studentHomeworkId).append("\n").append("课程：")
				.append(courseName).append("\n").append("作业：").append(exerciseName).append("\n"));
	}

}
