package com.liuhq.open.util;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.collect.Lists;
import com.liuhq.open.model.LoginVo;
import com.liuhq.open.model.QrCodeVo;
import com.liuhq.open.model.UniversityInfoVo;
import com.liuhq.open.model.UserVo;
import com.liuhq.open.model.WxParamterVo;

public class LoginUtils {

	private static WebClient client = WebClientFactory.getInstance();

	private static String DEFAULT_QR_CODE_PATH = "D://open_qr_code.jpg";

	private static String token;

	private static UniversityInfoVo universityInfo;

	public static UserVo login() {
		return login(DEFAULT_QR_CODE_PATH);
	}

	public static UserVo login(String filePath) {
		LoginVo loginVo = getWxImage(filePath);
		try {
			System.out.println("正在登录...");
			WebRequest request = new WebRequest(new URL(RequestUrlEnum.SCAN_CODE_LOGIN.getUrl()),
					RequestUrlEnum.SCAN_CODE_LOGIN.getMethod());
			request.setRequestParameters(Lists.<NameValuePair>newArrayList(
					new NameValuePair("code", loginVo.getWxCode()), new NameValuePair("state", loginVo.getState())));
			// 扫码之后调用的接口
			client.getPage(request);
			System.out.println("登录成功...");
			return getUserInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("登录失败");
	}

	/**
	 * 获取微信必须参数
	 */
	private static WxParamterVo getWeiXinCode() {
		try {
			WebRequest request = new WebRequest(new URL(RequestUrlEnum.GET_WEI_XIN_CODE.getUrl()),
					RequestUrlEnum.GET_WEI_XIN_CODE.getMethod());
			request.setRequestParameters(Lists.<NameValuePair>newArrayList(
					new NameValuePair("bust", String.valueOf(System.currentTimeMillis()))));
			UnexpectedPage page = client.getPage(request);
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
	private static QrCodeVo getWxImageInfo(WxParamterVo vo) {
		try {
			WebRequest request = new WebRequest(new URL(RequestUrlEnum.QRCONNECT.getUrl()),
					RequestUrlEnum.QRCONNECT.getMethod());
			request.setRequestParameters(Lists.<NameValuePair>newArrayList(new NameValuePair("appid", vo.getAppid()),
					new NameValuePair("scope", vo.getScope()), new NameValuePair("redirect_uri", vo.getRedirectUri()),
					new NameValuePair("state", vo.getState()), new NameValuePair("login_type", "jssdk"),
					new NameValuePair("self_redirect", "default"), new NameValuePair("styletype", vo.getStyle()),
					new NameValuePair("bgcolor", null), new NameValuePair("rst", null),
					new NameValuePair("href", vo.getHref())));
			HtmlPage page = client.getPage(request);
			// 这里需要停一下，因为获取uuid是异步加载js的，不然有可能获取不到uuid
			Thread.sleep(3000L);
			DomNodeList<DomElement> imgElementList = page.getElementsByTagName("img");
			if (imgElementList != null) {
				// 这个就是二维码的地址，通过获取src就能拿到了
				HtmlImage imgElement = imgElementList.stream()
						.filter(img -> img.getAttribute("class").contains("lightBorder") ? true : false).map(img -> {
							return (HtmlImage) img;
						}).findFirst().get();
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

	private static LoginVo getWxImage(String filePath) {
		try {
			// 这个是文件路径，默认D://open_qr_code.jpg
			filePath = filePath == null || "".equals(filePath) ? DEFAULT_QR_CODE_PATH : filePath;
			WxParamterVo vo = getWeiXinCode();
			QrCodeVo imgInfo = getWxImageInfo(vo);
			// 二维码存到本地
			FileOutputStream fos = new FileOutputStream(filePath);
			IOUtils.write(IOUtils.toByteArray(client.getPage("https://open.weixin.qq.com" + imgInfo.getQrCodeImgSrc())
					.getWebResponse().getContentAsStream()), fos);
			fos.close();
			// 打开图片
			Runtime.getRuntime().exec("cmd /c " + filePath);
			// 轮询获取wxCode参数
			String wxCode = "";
			while (true) {
				System.out.println("请扫码登录...");
				HtmlPage page = client.getPage("https://lp.open.weixin.qq.com/connect/l/qrconnect?uuid="
						+ imgInfo.getUuid() + "&_=" + System.currentTimeMillis());
				if (page.asText().contains("405")) {
					String str = page.asText();
					wxCode = str.split("window.wx_code='")[1].split("'")[0];
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

	private static UserVo getUserInfo() throws Exception {
		WebRequest request = new WebRequest(new URL("http://learn.open.com.cn/StudentCenter/user/getuserinfo"),
				HttpMethod.GET);
		UnexpectedPage page = client.getPage(request);
		String result = page.getWebResponse().getContentAsString();
		JSONObject data = JSONObject.parseObject(result).getJSONObject("data");
		return UserVo.builder().realname(data.getString("Realname")).universityStuNo(data.getString("UniversityStuNo"))
				.levelName(data.getString("LevelName")).specialityName(data.getString("SpecialityName"))
				.studentCode(data.getString("StudentCode")).mobilePhone(data.getString("MobilePhone")).build();
	}

	@SuppressWarnings("serial")
	public static String getToken() {
		if (token == null) {
			try {
				WebRequest request = new WebRequest(new URL(RequestUrlEnum.SIGN_CONTROLLER.getUrl()),
						RequestUrlEnum.SIGN_CONTROLLER.getMethod());
				request.setAdditionalHeaders(new HashMap<String, String>() {
					{
						put("Accept", "application/json, text/plain, */*");
						put("Content-Type", "application/json;charset=UTF-8");
						put("Sec-Fetch-Mode", "cors");
					}
				});
				request.setRequestBody(JSON.toJSONString(new HashMap<String, String>() {
					{
						put("sign", "a89033d60837737f944485229bcc82f1");
						put("timestamp", "1569850730");
					}
				}));
				UnexpectedPage page = client.getPage(request);
				String result = page.getWebResponse().getContentAsString();
				token = JSONObject.parseObject(result).getJSONObject("data").getString("token");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return token;
	}

	public static UniversityInfoVo getUniversityInfo() {
		if (universityInfo == null) {
			try {
				WebRequest request = new WebRequest(new URL(RequestUrlEnum.GET_UNIVERSITY_CODE.getUrl()),
						RequestUrlEnum.GET_UNIVERSITY_CODE.getMethod());
				UnexpectedPage page = client.getPage(request);
				String result = page.getWebResponse().getContentAsString();
				JSONObject data = JSONObject.parseObject(result).getJSONObject("data");
				universityInfo = UniversityInfoVo.builder().answerTime(data.getString("AnswerTime"))
						.batchId(data.getInteger("BatchId")).examineeId(data.getString("ExamineeId"))
						.judgeType(data.getString("JudgeType")).levelId(data.getInteger("LevelId"))
						.specialtyId(data.getInteger("SpecialtyId")).universityId(data.getString("UniversityId"))
						.universityCode(data.getString("universityCode")).build();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return universityInfo;
	}

}
