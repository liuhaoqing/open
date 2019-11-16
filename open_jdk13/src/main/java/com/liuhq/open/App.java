package com.liuhq.open;

import com.liuhq.open.service.impl.OpenServiceImpl;
import com.liuhq.open.service.impl.QrCodeScanLoginServiceImpl;

public class App {

	public static void main(String[] args) throws Exception {
		var loginService = new QrCodeScanLoginServiceImpl();
		// 登录
		var isLogin = loginService.login();
		if (isLogin) {
			var service = new OpenServiceImpl();
			// 开启多线程同时答题和看视频
			new Thread(service::autoAnswer).start();
			new Thread(service::antoWatchVideo).start();
		}
	}

}
