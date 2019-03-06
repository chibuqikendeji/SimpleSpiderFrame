package com.jsu.lqy.core.pipeline.impl;

import java.util.Map;

import com.jsu.lqy.core.pipeline.PipeLine;
import com.jsu.lqy.model.Page;
/**
 * 
 * @author Administrator
 * @date: 2019年3月4日 下午5:08:26 
 * @Description: 默认存储器，将html文件输出到控制台。
 */
public class ConsolePileLine implements PipeLine {

	@Override
	public void save(Page page) {
		Map<Object, Object> map = page.getItems();
	    map.forEach((k,v)->System.out.println(k+":"+v));
	}
}
