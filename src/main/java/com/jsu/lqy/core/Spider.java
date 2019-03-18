package com.jsu.lqy.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsu.lqy.core.dowmloader.impl.HttpClientDowmloader;
import com.jsu.lqy.core.downloader.DownLoader;
import com.jsu.lqy.core.pageprocessor.PageProcessor;
import com.jsu.lqy.core.pageprocessor.impl.PageProcessorImpl;
import com.jsu.lqy.core.pipeline.PipeLine;
import com.jsu.lqy.core.pipeline.impl.ConsolePileline;
import com.jsu.lqy.core.pipeline.impl.MybatisPipeline;
import com.jsu.lqy.core.scheduler.Scheduler;
import com.jsu.lqy.core.scheduler.impl.QueueScheduler;
import com.jsu.lqy.model.Page;
import com.jsu.lqy.model.UrlSeed;
import com.jsu.lqy.utils.TimeSleep;

public class Spider {
	private Logger logger = LoggerFactory.getLogger(getClass());
	// 定义四个模块
	private Scheduler scheduler;
	private DownLoader downLoader;
	private PageProcessor pageProcessor;
	private PipeLine pipeLine;
	// 线程池大小，默认是5
	private int threadNum = 5;
	// 定义线程池
	private ThreadPoolExecutor pool;
	public Scheduler getScheduler() {
		return scheduler;
	}
	public Spider setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
		return this;
	}
	public DownLoader getDownLoader() {
		return downLoader;
	}
	public Spider setDownLoader(DownLoader downLoader) {
		this.downLoader = downLoader;
		return this;
	}
	public PageProcessor getPageProcessor() {
		return pageProcessor;
	}
	public Spider setPageProcessor(PageProcessor pageProcessor) {
		this.pageProcessor = pageProcessor;
		return this;
	}
	public PipeLine getPipeLine() {
		return pipeLine;
	}
	public Spider setPipeLine(PipeLine pipeLine) {
		this.pipeLine = pipeLine;
		return this;
	}
	/**
	 * 设置最多几个爬虫同时进行
	 * 默认5个
	 * @param threadNum
	 * @return
	 */
	public Spider thread(int threadNum) {
        this.threadNum = threadNum;
        if (threadNum <= 0) {
            this.threadNum = 5;
        }
        pool = new ThreadPoolExecutor(threadNum, threadNum,
                1500L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        return this;
    }
	/**
	 * 生成爬虫
	 * @return
	 */
	public static Spider build() {
        return new Spider();
    }
	/**
	 * 创建实例
	 */
	public Spider() {
		setDefaultComponents();
	}
	/**
     * 添加url中子到scheduler中
     *
     * @param url url
     * @return Spider
     */
    public Spider addUrlSeed(String url) {
        scheduler.push(new UrlSeed(url));
        return this;
    }
	
	/**
	 * 设置默认组件
	 */
	private Spider setDefaultComponents(){
		// 线程池设置
		thread(threadNum);
		if (scheduler==null) {
			scheduler = new QueueScheduler();
		}
		if (downLoader==null) {
			downLoader = new HttpClientDowmloader();
		}
		if (pageProcessor==null) {
			pageProcessor = new PageProcessorImpl();
		}
		if (pipeLine==null) {
			pipeLine = new MybatisPipeline();
		}
		return this;
	}
	
	public void run() {
		logger.info("爬虫启动!");
		UrlSeed urlSeed = null;
        while (true) {
            logger.info("当前线程池" + "已完成:" + pool.getCompletedTaskCount() + "   运行中：" + pool.getActiveCount() + "  最大运行:" + pool.getPoolSize() + " 等待队列:" + pool.getQueue().size());
            if (pool.getQueue().size() > pool.getCorePoolSize()) {
                //如果等待队列大于了100.就暂停接收新的url。不然会影响优先级队列的使用。
                TimeSleep.sleep(1000);
                continue;
            }
            urlSeed = scheduler.poll();
            if (urlSeed == null && pool.getActiveCount() == 0) {
                pool.shutdown();
                try {
                    pool.awaitTermination(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    logger.error("关闭线程池失败！", e);
                }
                logger.info("爬虫结束！");
                break;
            } else if (urlSeed == null) {
                //没有取到种子就等待!
                TimeSleep.sleep(1000);
            } else {
                logger.info("正在处理:" + urlSeed.getUrl() + "  优先级(默认:5):" + urlSeed.getPriority());
                pool.execute(new Task(urlSeed));
            }
        }
	}
	
	class Task implements Runnable {
        private UrlSeed urlSeed;
        Task(UrlSeed urlSeed) {
            this.urlSeed = urlSeed;
        }
        public void run() {
            logger.debug("线程:[" + Thread.currentThread().getName() + "]正在处理:" + urlSeed.getUrl());
            logger.debug("当前线程池" + "已完成:" + pool.getCompletedTaskCount() + "   运行中：" + pool.getActiveCount() + "  最大运行:" + pool.getPoolSize() + " 等待队列:" + pool.getQueue().size());
            //整个流程为:
            // (download下载) ->  (pageProcessor解析处理) ->  (save存储)
            Page nowPage = downLoader.downLoader(urlSeed);
            pageProcessor.process(nowPage);
            if ((nowPage.getNewUrlSeed())!=null) {
            	nowPage.getNewUrlSeed().forEach(seed -> scheduler.push(seed));
            }
            if (nowPage.getDocument().equals("")) {
            	return;
            }
            pipeLine.save(nowPage);
        }
    }
}
