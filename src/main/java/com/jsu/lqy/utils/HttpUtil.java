package com.jsu.lqy.utils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

public class HttpUtil {
	// 创建httpclient连接池
    private PoolingHttpClientConnectionManager httpClientConnectionManager = null;
    private final int maxTotalPool = 200;    
    private final int maxConPerRoute = 20;
    private final int socketTimeout = 30000;
    private final int connectionRequestTimeout = 20000;
    private final int connectTimeout = 10000;
	
	//类初始化时，自动实例化，饿汉单例模式
    private static final HttpUtil httpUtils = new HttpUtil();
    public static HttpUtil getInstance() {
        return httpUtils;
    }
    private HttpUtil() {
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
            httpClientConnectionManager.setMaxTotal(maxTotalPool);
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
        		.build();
        return httpClient;
    }
    public String get(String urlString) {
    	RequestUtil request = new RequestUtil();
    	String src = request.getSrc(urlString);
    	return src; 
    }
}
