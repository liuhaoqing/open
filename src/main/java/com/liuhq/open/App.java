package com.liuhq.open;

import com.liuhq.open.model.UserVo;
import com.liuhq.open.service.OpenService;
import com.liuhq.open.util.LoginUtils;

public class App {

	public static void main(String[] args) throws Exception {
		UserVo user = LoginUtils.login();
		OpenService service = new OpenService();
		System.out.println(user);
		service.autoAnswer();
	}

}
