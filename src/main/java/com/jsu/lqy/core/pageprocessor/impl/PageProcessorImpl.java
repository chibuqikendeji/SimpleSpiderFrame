package com.jsu.lqy.core.pageprocessor.impl;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.jsu.lqy.core.pageprocessor.PageProcessor;
import com.jsu.lqy.model.Page;

public class PageProcessorImpl implements PageProcessor{
	/**
	 * 解析页面，筛选出需要的信息，保存在Page的List item中
	 */
	@Override
	public void process(Page page) {
        Document doc = page.getDocument();
        String title = doc.title();
        String text = doc.text();
        Map<String, Object> items = new HashMap<>();
        items.put("title", title);
        items.put("text", text);
        items.put("html", doc);
        page.setItems(items);
	}
	
}
