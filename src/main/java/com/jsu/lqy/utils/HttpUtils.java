package com.jsu.lqy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultBackoffStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.ByteArrayBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	// 创建httpclient连接池
    private PoolingHttpClientConnectionManager httpClientConnectionManager = null;
    private final int maxTotalPool = 200;    
    private final int maxConPerRoute = 20;
    private final int socketTimeout = 30000;
    private final int connectionRequestTimeout = 20000;
    private final int connectTimeout = 10000;
	private static String USER_AGENT[] = {
			"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; AcooBrowser; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
			"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Acoo Browser; SLCC1; .NET CLR 2.0.50727; Media Center PC 5.0; .NET CLR 3.0.04506)",
			"Mozilla/4.0 (compatible; MSIE 7.0; AOL 9.5; AOLBuild 4337.35; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36",
			"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 2.0.50727; Media Center PC 6.0)",
			"Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 1.0.3705; .NET CLR 1.1.4322)",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.20 (KHTML, like Gecko) Chrome/19.0.1036.7 Safari/535.20",
	};
	//类初始化时，自动实例化，饿汉单例模式
    private static final HttpUtils httpUtils = new HttpUtils();
    public static HttpUtils getInstance() {
        return httpUtils;
    }
    private HttpUtils() {
        init();
    }
    /**
     * 创建httpclient连接池，并初始化httpclient
     */
    public void init() {
        try {
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null,
                    new TrustSelfSignedStrategy())
                    .build();
            HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.getDefaultHostnameVerifier();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslsf)
                    .build();
            httpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // Increase max total connection to 200
            httpClientConnectionManager.setMaxTotal(maxTotalPool);
            // Increase default max connection per route to 20
            httpClientConnectionManager.setDefaultMaxPerRoute(maxConPerRoute);
            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(socketTimeout).build();
            httpClientConnectionManager.setDefaultSocketConfig(socketConfig);
        } catch (Exception e) {

        }
    }
    /**
     * 获取httpclient连接
     * @return
     */
    public CloseableHttpClient getHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
        		.setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout).build();
        CloseableHttpClient httpClient = HttpClients.custom()
        		// 传入连接池
        		.setConnectionManager(httpClientConnectionManager)
        		// 请求设置
        		.setDefaultRequestConfig(requestConfig)
        		// 设置禁止重定向
        		.setRedirectStrategy(new DefaultRedirectStrategy() {
        			@Override
        			protected boolean isRedirectable(String method) {
        				return false;
        			}
        		})
        		.build();
        return httpClient;
    }
    
	public String get(String urlString) {
		String src="";
		// 获取url种子中的url
		String url = urlString;
		if (url==null || !url.startsWith("http")) { //如果传入的url是空，或者不以http开头，则直接返回
			System.out.println("不存在该URL");
			return src;
		}
		// 去掉前后多余的空格
		url = url.trim();
		// 创建一个发送Http请求的对象
		CloseableHttpClient client = httpUtils.getHttpClient();
		// 创建一个接收响应的对象
		CloseableHttpResponse response = null;
		// 创建输入流
		InputStream in = null;
		// 构建请求
		HttpGet httpGet = null;
		
		// 修改url的请求头
		try {
			// 构建httpGet对象
			URL baseUrl = new URL(url);
			URI uri = new URI(baseUrl.getProtocol(), baseUrl.getHost(), baseUrl.getPath(), baseUrl.getQuery(), null);
			httpGet = new HttpGet(uri);
			// 设置请求头
			httpGet.addHeader("Accept", "*/*");
			httpGet.addHeader("Connection", "keep-alive");
			httpGet.addHeader("Accept-Encoding", "gzip, deflate");
			// 设置User-Agent
			Random random = new Random();
			Integer index = random.nextInt(4);
			httpGet.addHeader("User-Agent", USER_AGENT[index]);
			
			// 执行请求
			try {
				if (urlString.startsWith("https")) {
					// 设置系统属性，用于访问https开头的URL
				    System.setProperty("jsse.enableSNIExtension", "false");
				    response = httpUtils.getHttpClient().execute(httpGet);
				} else {
				    response = httpUtils.getHttpClient().execute(httpGet);
				}
			} catch (Exception e) {
				logger.error("执行httpClient.execute(httpGet)的异常", e);
			} 
			
			// 获得响应状态码
			Integer statusCode = response.getStatusLine().getStatusCode();
			// 根据状态码进行逻辑处理
			switch (statusCode) {
			case 200:
				// 获得响应实体
				HttpEntity entity = response.getEntity();
				// 获得响应流
			    in = entity.getContent();
				ByteArrayBuffer buffer = new ByteArrayBuffer(4096);// 存储读取到的字符
				byte[] tmp = new byte[4096];
				int count = 0; //记录读取到的字符
				while((count = in.read(tmp))!=-1) {
					buffer.append(tmp, 0, count);
				}
				src = new String(buffer.toByteArray());
				//System.out.println("src:"+src);
				break;
			case 400:
                logger.info("400:请求参数错误 " + urlString);
                break;
            case 403:
                logger.info("403:资源不可用 " + urlString);
                break;
            case 404:
                logger.info("404:资源不存在 " + urlString);
                break;
            case 503:
                logger.info("503:服务不可用 " + urlString);
                break;
            case 504:
                logger.info("504:网关超时 " + urlString);
                break;
			default:
				logger.info("未知错误 "+urlString);
				break;
			}
		} catch (MalformedURLException e) {
			//执行URL url = new URL()的异常
            logger.error("执行URL url = new URL()的异常", e);
		} catch (URISyntaxException e) {
			//执行URI uri = new URI()的异常
            logger.error("执行URL url = new URL()的异常", e);
		}catch (IOException e) {
			// 执行httpClient.execute(httpGet)的异常
            logger.error("执行httpClient.execute(httpGet)的异常", e);
		}finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("response.close()的异常", e);
                }
            }
            httpGet.abort();    //结束后关闭httpGet请求
        }
		
		return src;
	}
}
