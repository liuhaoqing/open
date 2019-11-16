package com.liuhq.open.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.liuhq.open.constant.OpenConstant;
import com.liuhq.open.constant.RequestUrlEnum;
import com.liuhq.open.exception.OpenException;
import com.liuhq.open.model.QrCodeDTO;
import com.liuhq.open.model.WeiXinCodeDTO;
import com.liuhq.open.service.LoginService;
import com.liuhq.open.utils.WebClientFactory;
import com.liuhq.open.utils.WebRequestFactory;

/**
 * 二维码扫描登录
 * 
 * @author liuhaoqing
 *
 */
public class QrCodeScanLoginServiceImpl implements LoginService {

	private WebClient client = WebClientFactory.getInstance();

	/**
	 * 二维码扫描登录
	 * 
	 * @return 登陆成功返回true，否则返回false
	 */
	@Override
	public boolean login() {
		var isLogin = false;
		//获取接口必要参数
		var weiXinCodeDTO = getWeiXinCode();
		var qrCodeDTO = getWxImageInfo(weiXinCodeDTO);
		// 获取扫码登录的二维码
		try (var img = getLoginQrCode(qrCodeDTO.getQrCodeImgSrc())) {
			Files.copy(img, Paths.get(OpenConstant.DEFAULT_QR_CODE_PATH), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenException("保存二维码失败");
		}
		// 打开图片
		try {
			Runtime.getRuntime().exec("cmd /c " + OpenConstant.DEFAULT_QR_CODE_PATH);
		} catch (IOException e) {
			e.printStackTrace();
			throw new OpenException("打开二维码失败");
		}
		// 轮询判断是否扫码登录
		while (true) {
			System.out.println("请扫码登录....");
			// 获取wxCode
			var wxCode = getWxCode(qrCodeDTO.getUuid());
			if (StringUtils.isNotBlank(wxCode)) {
				isLogin = true;
				// 登录之后需要再次调用另外一个接口，用来获取cookie
				scanCodeLogin(wxCode, weiXinCodeDTO.getState());
				break;
			}
			try {
				// 每1.5秒轮询一次
				Thread.sleep(1500L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return isLogin;
	}

	/**
	 * 获取登录信息
	 * 
	 * @param wxCode
	 * @param state
	 */
	private void scanCodeLogin(String wxCode, String state) {
		try {
			var request = WebRequestFactory.getInstance(RequestUrlEnum.SCAN_CODE_LOGIN);
			request.setRequestParameters(List.of(new NameValuePair("code", wxCode), new NameValuePair("state", state)));
			client.loadWebResponse(request);
			System.out.println("登录成功...");
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenException("登录失败");
		}
	}

	/**
	 * 轮询访问接口是否扫码登录
	 * 
	 * @param uuid
	 * @return 返回wxCode
	 */
	private String getWxCode(String uuid) {
		try {
			var request = WebRequestFactory.getInstance(RequestUrlEnum.LOGIN);
			request.setRequestParameters(List.of(new NameValuePair("uuid", uuid),
					new NameValuePair("_", String.valueOf(System.currentTimeMillis()))));

			var response = client.loadWebResponse(request);
			if (Objects.nonNull(response) && response.getStatusCode() == OpenConstant.HTTP_STATUS_CODE_OK) {
				var result = response.getContentAsString();
				if (result.contains("405")) {
					return result.split("window.wx_code='")[1].split("'")[0];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取登录二维码
	 * 
	 * @return
	 */
	private InputStream getLoginQrCode(String url) {
		try {
			var request = WebRequest.newAboutBlankRequest();
			request.setUrl(new URL(url));
			var response = client.loadWebResponse(request);
			if (Objects.nonNull(response) && response.getStatusCode() == OpenConstant.HTTP_STATUS_CODE_OK) {
				return response.getContentAsStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new OpenException("获取微信登录二维码失败");
	}

	/**
	 * 获取登录二维码地址及uuid
	 * 
	 * @param weiXinCodeDTO 登录二维码地址及uuid对象
	 */
	private QrCodeDTO getWxImageInfo(WeiXinCodeDTO weiXinCodeDTO) {
		try {
			var request = WebRequestFactory.getInstance(RequestUrlEnum.QR_CONNECT);
			request.setRequestParameters(List.of(new NameValuePair("appid", weiXinCodeDTO.getAppid()),
					new NameValuePair("scope", weiXinCodeDTO.getScope()),
					new NameValuePair("redirect_uri", weiXinCodeDTO.getRedirectUri()),
					new NameValuePair("state", weiXinCodeDTO.getState()), new NameValuePair("login_type", "jssdk"),
					new NameValuePair("self_redirect", "default"),
					new NameValuePair("styletype", weiXinCodeDTO.getStyle()), new NameValuePair("bgcolor", null),
					new NameValuePair("rst", null), new NameValuePair("href", weiXinCodeDTO.getHref())));
			var response = (HtmlPage) client.getPage(request);
			if (Objects.nonNull(response)
					&& response.getWebResponse().getStatusCode() == OpenConstant.HTTP_STATUS_CODE_OK) {
				var imgElement = response.getElementsByTagName("img").get(0);
				var src = imgElement.getAttribute("src");// 微信二维码路径后缀
				var split = src.split("/");// 最后一个就是uuid
				return QrCodeDTO.builder().qrCodeImgSrc(OpenConstant.OPEN_WEIXIN_BASE_URL + src)
						.uuid(split[split.length - 1]).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new OpenException("获取登录二维码地址及uuid失敗");
	}

	/**
	 * 获取微信二维码扫码登录必要参数
	 * 
	 * @return 微信二维码扫码登录必要参数
	 * @throws Exception
	 */
	private WeiXinCodeDTO getWeiXinCode() {
		try {
			var request = WebRequestFactory.getInstance(RequestUrlEnum.GET_WEI_XIN_CODE);
			request.setRequestBody(JSON.toJSONString(Map.of("bust", System.currentTimeMillis())));
			var response = client.loadWebResponse(request);
			if (Objects.nonNull(response) && response.getStatusCode() == OpenConstant.HTTP_STATUS_CODE_OK) {
				var result = response.getContentAsString();
				if (StringUtils.isNotBlank(result)) {
					// 解析参数
					var dataJSONObject = JSONObject.parseObject(result).getJSONObject("data");
					return WeiXinCodeDTO.builder().id(dataJSONObject.getString("id"))
							.appid(dataJSONObject.getString("appid")).scope(dataJSONObject.getString("scope"))
							.redirectUri(dataJSONObject.getString("redirect_uri"))
							.state(dataJSONObject.getString("state")).style(dataJSONObject.getString("style"))
							.href(dataJSONObject.getString("href")).build();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new OpenException("获取微信二维码扫码登录必要参数失败");
	}

}
