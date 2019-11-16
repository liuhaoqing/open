package com.liuhq.open.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录信息
 * 
 * @author liuhaoqing
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginInfoDTO implements Serializable {

	private static final long serialVersionUID = -3145177231503445735L;

	/** 姓名 **/
	private String realname;

	/** 学校编号 **/
	private String universityStuNo;

	/** 分数 **/
	private Integer totaPoints;

	/** 返回的是空字符串 **/
	private String studentStyleStr;

	/** 层次 **/
	private String levelName;

	/** 专业 **/
	private String specialityName;

	/** 返回的是null，不知道什么意思 **/
	private String url;

	/** **/
	private Boolean isYearRegInfo;

	/** 是否绑定 **/
	private Boolean isBind;

	/** 学号 **/
	private String studentCode;

	/** 手机号码 **/
	private String mobilePhone;

	/** 是否登录 **/
	private Boolean isLogin;

	/** 性别，01男 **/
	private String sex;

	/** 学校编号 **/
	private String universityCode;

	/** **/
	private String examineeId;

	/** **/
	private Date answerTime;

	/** **/
	private String universityId;

	/** **/
	private Long batchId;

	/** **/
	private Long levelId;

	/** **/
	private Long specialtyId;

	/** **/
	private String judgeType;
	
	/** **/
	private String token;

}
