package com.jsu.lqy.core.scheduler;

import com.jsu.lqy.model.UrlSeed;

/**
 * 调度器。
 * 不需要进行修改
 * 除非你需要自己实现优先级调度
 * 实现去重功能
 * @author Administrator
 *
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
