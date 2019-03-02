package com.jsu.lqy.core.downloader;

import com.jsu.lqy.model.Page;
import com.jsu.lqy.model.UrlSeed;

/**
 * 下载器
 * @author Administrator
 *
 */
public interface DownLoader {
	Page downLoader(UrlSeed urlseed);
}
