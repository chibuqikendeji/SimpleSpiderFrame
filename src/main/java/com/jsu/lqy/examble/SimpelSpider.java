package com.jsu.lqy.examble;

import com.jsu.lqy.core.Spider;

public class SimpelSpider {
    public static void main(String[] args) {
		Spider.build()
		.addUrlSeed("https://www.baidu.com/")
		.run();
	}
}
