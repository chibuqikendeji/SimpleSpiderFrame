package com.jsu.lqy.core.pipeline.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsu.lqy.core.pipeline.PipeLine;
import com.jsu.lqy.model.Page;
import com.jsu.lqy.utils.FilePersistentBase;

public class FilePipeLine extends FilePersistentBase implements PipeLine{
    private Logger logger = LoggerFactory.getLogger(Logger.class);
    
    public FilePipeLine() {
    	// 默认文件存储位置
    	setPath("E:/data/SimpleSpiderFrame/");
    }
    public FilePipeLine(String path) {
    	setPath(path);
    }
    
    /**
     * 将url和html写入指定文件
     */
	@Override
	public void save(Page page) {
        String path = this.path+PATH_SEPERATOR+System.currentTimeMillis()+PATH_SEPERATOR;
        try {
			PrintWriter pw = new PrintWriter(
					new OutputStreamWriter(
							new FileOutputStream(
									// 设置保存为txt文件或者html文件
									getFile(path+DigestUtils.md5Hex(page.getUrlSeed().getUrl())+".txt")),"UTF-8"));
        	pw.println("url:\t"+page.getUrlSeed().getUrl());
        	Map<Object, Object> items = page.getItems();
        	for (Entry<Object, Object> entry : items.entrySet()) {
        		if (entry instanceof Iterable) {
        			Iterable value = (Iterable) entry.getValue();
        			pw.println(entry.getKey()+":\\t");
        			value.forEach((o)->pw.println(o));
        		}else {
        			pw.println(entry.getKey() + ":\t" + entry.getValue());
        		}
        	}
        	pw.close();
        	System.out.println("文件保存完成");
		} catch (IOException e) {
			logger.warn("文件保存失败");
		}
	}
}