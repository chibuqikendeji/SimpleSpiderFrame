package com.jsu.lqy.core.pipeline.impl;

import org.apache.ibatis.session.SqlSession;

import com.jsu.lqy.core.pipeline.PipeLine;
import com.jsu.lqy.dao.WebDao;
import com.jsu.lqy.model.Page;
import com.jsu.lqy.model.Web;
import com.jsu.lqy.utils.MybatisUtil;
/**
 * 
 * @author lanqiyu
 * @date: 2019年3月18日 上午11:32:10 
 * @Description: 使用mybatis存储数据
 */
public class MybatisPipeline implements PipeLine{
	@Override
	public void save(Page page) {
       Web web = new Web();
       web.setTitle(page.getDocument().title());
       web.setUrl(page.getUrlSeed().getUrl());
       synchronized (this){
    	   SqlSession session = MybatisUtil.newInstance().getSession();
    	   WebDao dao = session.getMapper(WebDao.class);
    	   dao.insert(web);
    	   session.commit();
    	   session.close();
       }
	}
}
