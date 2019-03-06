package com.jsu.lqy.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Page implements Serializable{
	private static final long serialVersionUID = 8795321664186547978L;
	// 保存该页面的jsoup文档，设置了baseUrl
	private Document document;
	// 保存该页面的url信息
	private UrlSeed urlSeed;
	//新种子
    private List<UrlSeed> newUrlSeed;
    //待存储的信息
    private Map<Object, Object> items;
	
	public Page(String html, UrlSeed urlSeed) {
		super();
		this.document = Jsoup.parse(html,urlSeed.getUrl());
		this.urlSeed = urlSeed;
	}
	public Document getDocument() {
		return document;
	}
	public Page setDocument(Document document) {
		this.document = document;
		return this;
	}
	public UrlSeed getUrlSeed() {
		return urlSeed;
	}
	public Page setUrlSeed(UrlSeed urlSeed) {
		this.urlSeed = urlSeed;
		return this;
	}
	public List<UrlSeed> getNewUrlSeed() {
		return newUrlSeed;
	}
	public Page setNewUrlSeed(List<UrlSeed> newUrlSeed) {
		this.newUrlSeed = newUrlSeed;
		return this;
	}
	public Map<Object, Object> getItems() {
		return items;
	}
	public Page setItems(Map items) {
		this.items = items;
		return this;
	}
	@Override
	public String toString() {
		return "Page [document=" + document + ", urlSeed=" + urlSeed + ", newUrlSeed=" + newUrlSeed + ", items=" + items
				+ "]";
	}
	
}
