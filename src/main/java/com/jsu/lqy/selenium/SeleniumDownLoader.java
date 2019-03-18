package com.jsu.lqy.selenium;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsu.lqy.core.downloader.DownLoader;
import com.jsu.lqy.model.Page;
import com.jsu.lqy.model.UrlSeed;

public class SeleniumDownLoader implements DownLoader{
    private Logger logger = LoggerFactory.getLogger(SeleniumDownLoader.class);
    private Integer sleepTime = 10;
    public SeleniumDownLoader() {
    	System.setProperty("webdriver.chrome.driver", "E:./src/main/resources/chromedriver.exe");		
	}
    public SeleniumDownLoader(String chromeDriverPath) {
    	System.setProperty("webdriver.chrome.driver", chromeDriverPath);		
	}
	public Integer getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(Integer sleepTime) {
		this.sleepTime = sleepTime;
	}
	@Override
	public Page downLoader(UrlSeed urlseed) {
		logger.info("正在爬取"+urlseed.getUrl());
        WebDriver driver = new ChromeDriver();
        driver.get(urlseed.getUrl());
        driver.manage().window().maximize();
        
        try {
			Thread.sleep(sleepTime*1000);
		} catch (InterruptedException e) {
			logger.warn("打开浏览器出错");
		}
        /*WebElement findElement = driver.findElement(By.xpath("/html"));
        String html = findElement.getAttribute("outHTML");*/
        String html = driver.getPageSource();
        Document document = Jsoup.parse(html,urlseed.getUrl());
        driver.close();
        Page page = new Page(html, urlseed);
        page.setDocument(document);
        Map<String, Object> items = new HashMap<>();
        items.put("title", document.title());
        items.put("html", document);
        items.put("url", urlseed.getUrl());
        page.setItems(items);
		return page;
	}
        
	public static void main(String[] args) {
		SeleniumDownLoader downloader = new SeleniumDownLoader();
		Page page = downloader.downLoader(new UrlSeed("https://lol.qq.com/"));
		System.out.println(page.getDocument());
		System.out.println(page.getUrlSeed().toString());
	}
}
