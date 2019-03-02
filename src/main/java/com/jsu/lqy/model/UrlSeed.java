package com.jsu.lqy.model;

/**
 * 创建UrlSeed对象
 * @author Administrator
 *
 */
public class UrlSeed{
	// 用户传入的URL
	private String url;
	// 设置优先级，默认为5
	private long priority = 5;
	
	// 两个构造方法，设置URL种子和优先级
	public UrlSeed(String url) {
		this.url = url;
		this.priority = 5;
	}
	public UrlSeed(String url, long priority) {
		this.url = url;
		this.priority = priority;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getPriority() {
		return priority;
	}
	public void setPriority(long priority) {
		this.priority = priority;
	}
	@Override
	public String toString() {
		return "UrlSeed [url=" + url + ", priority=" + priority + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (priority ^ (priority >>> 32));
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UrlSeed other = (UrlSeed) obj;
		if (priority != other.priority)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}
