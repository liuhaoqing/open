package com.liuhq.open.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;

public class WebClientFactory {

	private static final WebClient client;

	static {
		client = new WebClient(BrowserVersion.CHROME);
		WebClientOptions options = client.getOptions();
		options.setJavaScriptEnabled(true);
		options.setCssEnabled(false);
		options.setThrowExceptionOnFailingStatusCode(false);
	}

	public static WebClient getInstance() {
		return client;
	}

}
