package com.jsu.lqy.core.pipeline.impl;

import java.util.HashMap;
import java.util.Map;

import com.jsu.lqy.core.pipeline.PipeLine;
import com.jsu.lqy.model.Page;

public class ConsolePileLine implements PipeLine {

	@Override
	public void save(Page page) {
		Map<Object, Object> map = page.getItems();
	    map.forEach((k,v)->System.out.println(k+":"+v));
	}
}
