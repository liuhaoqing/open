package com.liuhq.open.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVo {

	private String realname;

	private String universityStuNo;

	private String levelName;

	private String specialityName;

	private String studentCode;

	private String mobilePhone;
	
	@Override
	public String toString() {
		return new String(new StringBuffer().append("姓名：").append(realname).append("\n").append("学号：")
				.append(universityStuNo).append("\n").append("层次：").append(levelName).append("\n").append("专业：")
				.append(specialityName).append("\n").append("编号：").append(studentCode).append("\n").append("手机：")
				.append(mobilePhone).append("\n"));
	}

}
