package com.jsu.lqy.core.dowmloader.impl;

import com.jsu.lqy.core.downloader.DownLoader;
import com.jsu.lqy.model.Page;
import com.jsu.lqy.model.UrlSeed;
import com.jsu.lqy.utils.HttpUtil;
/**
 * 
 * @author Administrator
 * @date: 2019年3月4日 下午5:04:40 
 * @Description: 下载器。负责调用工具进行html文件下载，并返回Page对象
 */
public class HttpClientDowmloader implements DownLoader{
	@Override
	public Page downLoader(UrlSeed urlseed) {
		String urlString = urlseed.getUrl();
		String src = HttpUtil.getInstance().get(urlString);
		Page page = new Page(src, urlseed);
		return page;
	}	
}
