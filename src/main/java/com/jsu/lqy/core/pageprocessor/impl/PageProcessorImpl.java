package com.jsu.lqy.core.pageprocessor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jsu.lqy.core.pageprocessor.PageProcessor;
import com.jsu.lqy.model.Page;
import com.jsu.lqy.model.UrlSeed;
/**
 * 
 * @author Administrator
 * @date: 2019年3月4日 下午5:06:21 
 * @Description: 解析页面，筛选出需要的信息，保存在Page的List item中
 */
public class PageProcessorImpl implements PageProcessor{
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void process(Page page) {
		if (page.getDocument().equals("")) {
			return;
		}
        Document doc = page.getDocument();
        Map<String, Object> items = new HashMap<>();
        items.put("url", page.getUrlSeed().getUrl());
        items.put("title", doc.title());
        items.put("html", doc);
        List<UrlSeed> newUrlSeedList = new ArrayList<UrlSeed>();
        Elements elementsByTag = doc.getElementsByTag("a");
        for (Element e : elementsByTag) {
        	String url = e.absUrl("abs:href");
        	newUrlSeedList.add(new UrlSeed(url));
		}
        page.setNewUrlSeed(newUrlSeedList);
        page.setItems(items);
	}
}
