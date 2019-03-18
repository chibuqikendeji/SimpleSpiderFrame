package com.jsu.lqy.examble;

import com.jsu.lqy.core.Spider;

public class SimpleSpiderTest {
    public static void main(String[] args) {
		Spider.build()
		.addUrlSeed("http://cise.jsu.edu.cn/index.htm")
		.thread(6)
		.run();
	}
}
