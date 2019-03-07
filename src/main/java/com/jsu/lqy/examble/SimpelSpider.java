package com.jsu.lqy.examble;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jsu.lqy.core.Spider;
import com.jsu.lqy.core.pageprocessor.PageProcessor;
import com.jsu.lqy.core.pipeline.PipeLine;
import com.jsu.lqy.model.Page;
import com.jsu.lqy.model.UrlSeed;
import com.jsu.lqy.utils.JsonUtils;

/**
 * 根据一个规则，爬取学院官网上的符合此规则的新闻内容
 * @author lanqiyu
 * @date: 2019年3月7日 下午7:22:37 
 * @Description: 该类的功能描述
 */
public class SimpelSpider {
    public static void main(String[] args) {
		Spider.build()
		.addUrlSeed("http://cise.jsu.edu.cn/index.htm")
		.setPageProcessor(new myProcessor())
		.setPipeLine(new myPipeLine())
		.thread(10)
		.run();
	}
}

/**
 * 
 * @author Administrator
 * @date: 2019年3月7日 下午1:55:24
 * @Description: 页面解析
 */
class myProcessor implements PageProcessor{
	private String baseUrl = "http://cise.jsu.edu.cn/";
	private String regex = ".*info/\\d*/\\d*.htm";
	private List<UrlSeed> urlSeedList = new ArrayList<>();
	@Override
	public void process(Page page) {
		if (Pattern.matches(regex, page.getUrlSeed().getUrl())) {
			String context = "";
			Map<String, Object> items = new HashMap<>();
			String cssQuery = "#vsb_content > div > p";
			Document d = page.getDocument();
        	Elements select = d.select(cssQuery);
        	for (Element e : select) {
        		context = context+e.text();
			}
        	items.put("title", page.getDocument().title());
        	items.put("Context", context);
        	page.setItems(items);
        	return;
        }
		Document doc = page.getDocument();               
        Elements tags = doc.getElementsByAttributeValueMatching("href", regex);
        for (Element e : tags) {
        	urlSeedList.add(new UrlSeed(baseUrl+e.attr("href")));
		}
        page.setNewUrlSeed(urlSeedList);
	}
}
/**
 * 
 * @author Administrator
 * @date: 2019年3月7日 下午1:56:08 
 * @Description: 存储器
 */
class myPipeLine implements PipeLine{
	public static String path = "E:\\cise.jsu\\";
	@Override
	public void save(Page page) {
        File fileRoot = new File(path);
        if (!fileRoot.exists()&&fileRoot.isDirectory()) {
        	System.out.println("目标文件夹不存在，开始创建");
        	if (fileRoot.mkdirs()) {
        		System.out.println("文件"+path+"创建成功");
        	}else {
        		System.out.println("目标文件夹不存在，创建失败");
                System.exit(-1);
        	}
        }
        if (page.getItems()!=null) {
        	String name = (String) page.getItems().get("title");
        	if (name==null || name.equals("")) {
        		return;
        	}
        	String json = JsonUtils.toJsonBeautiful(page.getItems());
        	// 将json串存入文件中
        	try {
        		FileUtils.write(new File(path+name+".json"), json,"utf-8");
        		System.out.println("文件保存成功："+path+name+".json");
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
	}
	
}
