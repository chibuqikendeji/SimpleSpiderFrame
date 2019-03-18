package com.jsu.lqy.utils;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.jsu.lqy.dao.WebDao;
import com.jsu.lqy.model.Web;
/**
 * 
 * @author lanqiyu
 * @date: 2019年3月18日 上午11:32:28 
 * @Description: mybatis工具类
 */
public class MybatisUtil {
	private static SqlSessionFactory ssf;
	private static final MybatisUtil mybatis = new MybatisUtil();
	private MybatisUtil() {
		try {
			init();
		} catch (Exception e) {
			System.out.println("mybatis初始化错误");
		}
	}
	public static MybatisUtil newInstance() {
		return mybatis;
	}
	// 初始化SqlSessionFactory
    public void init() throws Exception {
    	InputStream in = Resources.getResourceAsStream("sqlMapperConfig.xml");
    	ssf = new SqlSessionFactoryBuilder().build(in);
    }
    public SqlSession getSession() {
    	SqlSession session = ssf.openSession();
    	return session;
    }
}
