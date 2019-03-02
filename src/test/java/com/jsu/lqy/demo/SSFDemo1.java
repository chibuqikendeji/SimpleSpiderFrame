package com.jsu.lqy.demo;

import com.jsu.lqy.core.dowmloader.impl.HttpClientDowmloader;
import com.jsu.lqy.model.UrlSeed;

public class SSFDemo1 {
	public static void main(String[] args) {
		HttpClientDowmloader downloader = new HttpClientDowmloader();
		UrlSeed seed = new UrlSeed("http://www.baidu.com");
		downloader.downLoader(seed);
	}
	
}
