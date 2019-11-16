package com.liuhq.open.utils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;

public class WebClientFactory {
	
	private static final WebClient client;

	static {
		client = new WebClient();
		WebClientOptions options = client.getOptions();
		options.setJavaScriptEnabled(false);
		options.setCssEnabled(false);
		options.setThrowExceptionOnFailingStatusCode(false);
	}

	public static WebClient getInstance() {
		return client;
	}
	
}
