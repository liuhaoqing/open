package com.liuhq.open;

import com.liuhq.open.model.UserVo;
import com.liuhq.open.service.OpenService;
import com.liuhq.open.util.LoginUtils;

/**
 * Hello world!
 *
 */
public class App {
	
	public static void main(String[] args) throws Exception {
		OpenService service = new OpenService();
		UserVo user = LoginUtils.login();
		System.out.println(user);
		service.autoAnswer();
	}

}
