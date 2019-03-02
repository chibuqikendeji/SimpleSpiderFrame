package com.jsu.lqy.clienttest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpClientTest {
	@Test
	public void Test1() throws Exception {
		//需要访问的URL
		String url = "http://www.baidu.com";
		
		
		 // 1.使用默认配置的httpclient,创建一个CloseableHttpClient对象
		 CloseableHttpClient client = HttpClients.createDefault();
		 // 2.构建一个请求对象
		 HttpGet httpGet = new HttpGet(url);
		 InputStream in = null;
		 BufferedReader bf = null;
		 CloseableHttpResponse response = null;
		 // 3.执行请求，获取响应
		 try {
			response = client.execute(httpGet);
			// 打印返回状态码
			System.out.println(response.getStatusLine().getStatusCode());
			// 4.获取响应的实体内容，即网页源码
			HttpEntity entity = response.getEntity();
			// 5.将结果打印到控制台上
			// 5.1 使用EntityUtils工具类输出
//			if (entity != null) {
//				System.out.println(EntityUtils.toString(entity, "utf-8"));   // 不推荐
//			}
			// 5.2 使用inputstream输出
			// 得到返回的实体内容
			in = entity.getContent(); 
			//包装到BufferedRead中
			bf = new BufferedReader(new InputStreamReader(in,"utf-8"));
			String line = "";
			while((line=bf.readLine())!=null) {
				System.out.println(line);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(in!=null) {
				in.close();
			}
			if(response!=null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					response = null;
				}
			}
		}
		 
	}
}
