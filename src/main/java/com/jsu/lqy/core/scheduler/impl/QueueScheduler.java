package com.jsu.lqy.core.scheduler.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsu.lqy.core.scheduler.Scheduler;
import com.jsu.lqy.model.UrlSeed;
/**
 * 
 * @author Administrator
 * @date: 2019年3月4日 下午5:09:47 
 * @Description: 调度器。
 * 使用基于优先级的队列进行urlSeed的去重与排序。
 */
public class QueueScheduler implements Scheduler{
	// 设置队列的初始大小
	public static final int defaultPriority = 5;
	// 优先级队列，传入比较器
	private PriorityQueue<UrlSeed> queue = 
			new PriorityQueue<>(defaultPriority, 
					new Comparator<UrlSeed>() {
				// 降序排序
						@Override
						public int compare(UrlSeed o1, UrlSeed o2) {
							return (int) (o2.getPriority()-o1.getPriority());
						}
	});
	// 创建一个线程安全的Set，用于url的去重
	private Set<UrlSeed> urlSet = Collections.synchronizedSet(new HashSet<>());
	@Override
	public void push(UrlSeed urlSeed) {
		String url = urlSeed.getUrl().trim();
        if (url==null
        		|| url.equals("")
        		|| url.equals("#")
        		|| url.toLowerCase().contains("javascript:")) {
        	return;
        }
        if (urlSet.add(urlSeed)) {
        	queue.add(urlSeed);
        }else {}
	}
	@Override
	public UrlSeed poll() {
		return queue.poll();
	}
}
