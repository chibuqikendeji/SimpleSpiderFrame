package com.jsu.lqy.core.pageprocessor;

import com.jsu.lqy.model.Page;

/**
 * 这个模块的作用：
 *     对数据的筛选和处理
 * process(Page page)函数的作用：
 *     1.解析需要的数据，放入Page的List item中，之后Pipeline模块会进行数据持久化
 *     2.解析新的url种子，放入Page的List newUrlSeed中。
 * @author Administrator
 *
 */
public interface PageProcessor {
	void process(Page page);
}
