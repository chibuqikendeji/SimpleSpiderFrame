package com.jsu.lqy.core.scheduler;

import com.jsu.lqy.model.UrlSeed;

/**
 * 
 * @author lanqiyu
 * @date: 2019年2月14日 上午11:21:41 
 * @Description: 调度器。
 * 不需要进行修改
 * 除非你需要自己实现优先级调度
 * 实现去重功能
 */
public interface Scheduler {
	/**
	 * 写入URL种子
	 */
	void push(UrlSeed urlSeed);
	/**
	 * 取出种子
	 */
	UrlSeed poll();
}
