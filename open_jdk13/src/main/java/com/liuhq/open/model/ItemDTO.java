package com.liuhq.open.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {

	private String i1;
	
	/** 题目 **/
	private String i2;
	
	private Long i3;
	
	private String i4;
	
	private String i5;
	
	/** 选项 **/
	private List<String> i6;
	
	/** 这个就是答案 **/
	private List<String> i7;
	
	private List<String> i8;
	
	private Double i9;
	
	private String i10;
	
	private Long i11;
	
	private String i12;
	
	private Double i13;
	
	private String i14;
	
	private List<Object> i15;
	
	private Double i16;
	
	private Long i17;
	
	private String i18;
	
	private Long i19;
	
	private Long i20;
	
	private String i21;
	
	private String i22;
	
	private Date i23;
	
	private Object i24;
	
	private Long i25;
	
	private Long i26;
	
	private Long i27;
	
	private Long i28;
	
	private Date i29;
	
	private String i30;
	
	private Long i31;
	
	private Long i32;
	
	private String i33;
	
	private Long i34;
	
	private Long i35;
	
	private List<Object> sub;
	
	private List<ChooseDTO> choices;
	
	private List<Object> knowledges;
	
	private Object knowledgesJson;
	
}
