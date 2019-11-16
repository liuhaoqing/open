package com.liuhq.open.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录二维码地址及uuid
 * 
 * @author liuhaoqing
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrCodeDTO implements Serializable{

	private static final long serialVersionUID = -3440119392236919130L;

	private String qrCodeImgSrc;

	private String uuid;

}
