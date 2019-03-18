package com.jsu.lqy.demo;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.jsu.lqy.dao.WebDao;
import com.jsu.lqy.model.Web;

public class SSFDemo1 {
	public static void main(String[] args) throws Exception {
		/*HttpClientDowmloader downloader = new HttpClientDowmloader();
		UrlSeed seed = new UrlSeed("http://www.baidu.com");
		downloader.downLoader(seed);*/
		SqlSessionFactory ssf;
		InputStream in = Resources.getResourceAsStream("sqlMapperConfig.xml");
    	ssf = new SqlSessionFactoryBuilder().build(in);
    	SqlSession session = ssf.openSession();
    	WebDao dao = session.getMapper(WebDao.class);
    	Web web = new Web();
    	web.setHtml("abc");
    	web.setTitle("abc");
    	web.setUrl("abc");
    	dao.insert(web);
    	session.commit();
    	System.out.println(session);
    	System.out.println(dao);
    	System.out.println(web);
    	session.close();
	}
	
}
