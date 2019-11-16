package com.liuhq.open.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.collect.Lists;
import com.liuhq.open.constant.OpenConstant;
import com.liuhq.open.model.LoginVo;
import com.liuhq.open.model.QrCodeVo;
import com.liuhq.open.model.WxParamterVo;
import com.liuhq.open.util.RequestUrlEnum;
import com.liuhq.open.util.WebClientFactory;
import com.liuhq.open.util.WebRequestFactory;

public class LoginService {

	private WebClient client = WebClientFactory.getInstance();

	public LoginVo getWxImage(String filePath) {
		try {
			// 这个是文件路径
			filePath = Objects.isNull(filePath) || "".equals(filePath) ? OpenConstant.DEFAULT_QR_CODE_PATH : filePath;
			WxParamterVo vo = getWeiXinCode();
			QrCodeVo imgInfo = getWxImageInfo(vo);
			// 二维码存到本地
			InputStream img = client.getPage("https://open.weixin.qq.com" + imgInfo.getQrCodeImgSrc()).getWebResponse()
					.getContentAsStream();
			Files.copy(img, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
			img.close();
			// 打开图片
			Runtime.getRuntime().exec("cmd /c " + filePath);
			// 轮询获取wxCode参数
			String wxCode = "";
			while (true) {
				System.out.println("请扫码登录...");
				HtmlPage page = client.getPage(WebRequestFactory.getInstance(RequestUrlEnum.LOGIN,
						Lists.<NameValuePair>newArrayList(new NameValuePair("uuid", imgInfo.getUuid()),
								new NameValuePair("_", String.valueOf(System.currentTimeMillis())))));
				if (page.asText().contains("405")) {
					wxCode = page.asText().split("window.wx_code='")[1].split("'")[0];
					break;
				}
				// 每2秒轮询一次
				Thread.sleep(2000L);
			}
			System.out.println("扫码成功...");
			return LoginVo.builder().state(vo.getState()).wxCode(wxCode).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("获取微信二维码失败");
	}

	/**
	 * 获取微信必须参数
	 */
	private WxParamterVo getWeiXinCode() {
		try {
			UnexpectedPage page = client.getPage(
					WebRequestFactory.getInstance(RequestUrlEnum.GET_WEI_XIN_CODE, Lists.<NameValuePair>newArrayList(
							new NameValuePair("bust", String.valueOf(System.currentTimeMillis())))));
			String result = page.getWebResponse().getContentAsString();
			if (StringUtils.isNotBlank(result)) {
				JSONObject data = JSONObject.parseObject(result).getJSONObject("data");
				return WxParamterVo.builder().id(data.getString("id")).appid(data.getString("appid"))
						.scope(data.getString("scope")).redirectUri(data.getString("redirect_uri"))
						.state(data.getString("state")).style(data.getString("style")).href(data.getString("href"))
						.build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("获取微信参数失败！");
	}

	/**
	 * 获取登录二维码地址及uuid
	 */
	private QrCodeVo getWxImageInfo(WxParamterVo vo) {
		try {
			HtmlPage page = client.getPage(WebRequestFactory.getInstance(RequestUrlEnum.QRCONNECT,
					Lists.<NameValuePair>newArrayList(new NameValuePair("appid", vo.getAppid()),
							new NameValuePair("scope", vo.getScope()),
							new NameValuePair("redirect_uri", vo.getRedirectUri()),
							new NameValuePair("state", vo.getState()), new NameValuePair("login_type", "jssdk"),
							new NameValuePair("self_redirect", "default"),
							new NameValuePair("styletype", vo.getStyle()), new NameValuePair("bgcolor", null),
							new NameValuePair("rst", null), new NameValuePair("href", vo.getHref()))));
			// 这里需要停一下，因为获取uuid是异步加载js的，不然有可能获取不到uuid
			Thread.sleep(3000L);
			DomNodeList<DomElement> imgElementList = page.getElementsByTagName("img");
			if (Objects.nonNull(imgElementList)) {
				// 这个就是二维码的地址，通过获取src就能拿到了
				HtmlImage imgElement = imgElementList.stream()
						.filter(img -> img.getAttribute("class").contains("lightBorder") ? true : false)
						.map(img -> (HtmlImage) img).findFirst().get();
				// 这里是获取uuid，这个uuid是异步加载js实现的
				DomNodeList<DomElement> scriptElementList = page.getElementsByTagName("script");
				HtmlScript uuidScript = scriptElementList.stream().map(scriptElement -> (HtmlScript) scriptElement)
						.filter(scriptElement -> "".equals(scriptElement.getAttribute("async"))
								&& scriptElement.getSrcAttribute().contains("uuid") ? true : false)
						.findFirst().get();
				String uuid = uuidScript.getSrcAttribute().substring(55, 71);
				return QrCodeVo.builder().qrCodeImgSrc(imgElement.getSrcAttribute()).uuid(uuid).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("获取微信二维码失败！");
	}

}
