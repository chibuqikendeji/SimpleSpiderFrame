package com.jsu.lqy.core.dowmloader.impl;

import com.jsu.lqy.core.downloader.DownLoader;
import com.jsu.lqy.model.Page;
import com.jsu.lqy.model.UrlSeed;
import com.jsu.lqy.utils.HttpUtils;

public class HttpClientDowmloader implements DownLoader{
	@Override
	public Page downLoader(UrlSeed urlseed) {
		String urlString = urlseed.getUrl();
		String src = HttpUtils.getInstance().get(urlString);
		Page page = new Page(src, urlseed);
		return page;
	}	
}
