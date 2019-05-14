package com.jsu.lqy.example;

import com.jsu.lqy.core.Spider;
import com.jsu.lqy.core.pipeline.impl.FilePipeline;

public class SimpleSpiderTest {
    public static void main(String[] args) {
		Spider.build()
		.addUrlSeed("http://cise.jsu.edu.cn/index.htm")
		//.setPageProcessor(new MyProcessor())
		//.setPipeLine(new FilePipeline())
		.run();
	}
}