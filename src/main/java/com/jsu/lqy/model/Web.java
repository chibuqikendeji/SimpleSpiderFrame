package com.jsu.lqy.model;

public class Web {
    private String url;
    private String title;
    private String html;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	@Override
	public String toString() {
		return "Web [url=" + url + ", title=" + title + ", html=" + html + "]";
	}
    
}
