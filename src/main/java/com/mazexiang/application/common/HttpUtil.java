/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.mazexiang.application.common;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


/**
 * @author mazexiang
 * @version $Id: HttpUtil, v 0.1 2020-11-07 5:23 PM mzx Exp $
 */
public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);
    /**
     * 线程安全的连接管理器,多线程连接时保证连接的线程安全
     */
    protected static HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
    private static HttpConnectionManagerParams httpConnParams = new HttpConnectionManagerParams();
    private static HttpClientParams httpClientParams = new HttpClientParams();
    protected static HttpClient httpClient = null;
    private static final int READ_TIMEOUT_TIME = 30000;

    /**
     * 默认构造函数
     */
    static {
        if (httpClient == null) {
            //每个主机地址最大连接数
            httpConnParams.setDefaultMaxConnectionsPerHost(128);
            //所有主机地址最大连接数,多主机地址的连接使限制所有主机地址总的连接数
            httpConnParams.setMaxTotalConnections(1024);
            //连接超时时间
            httpConnParams.setConnectionTimeout(1500);
            httpConnParams.setSoTimeout(READ_TIMEOUT_TIME);
            //每次使用连接前检查连接状态是否可用
            httpConnParams.setStaleCheckingEnabled(true);
            //设置认证方式为抢占式。这种方式HttpClient会发送基本的认证Response,有些情况下,会在服务器返回一个未经授权的Response之前就发出去,从而使连接减少了开销。
            httpClientParams.setAuthenticationPreemptive(true);
            //通知服务器,在解析Cookie的时候采用尽可能兼容所有浏览器的方案
            httpClientParams.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            //发起请求时会将所有的Cookie合并成一条放入请求头信息中
            httpClientParams.setParameter("http.protocol.single-cookie-header", true);

            httpConnectionManager.setParams(httpConnParams);
            httpClient = new HttpClient(httpClientParams, httpConnectionManager);
        }
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    public static String httpGet(String url) {

        String response = "";
        HttpClient client = getHttpClient();
        GetMethod getMethod = new GetMethod(url);
        String suc= "N";
        long timeStart = System.currentTimeMillis();

        int status = 0;
        try {
            status = client.executeMethod(getMethod);
            //校验返回状态码
            if (status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED) {

                String encodec = getMethod.getResponseCharSet();
                if (StringUtil.equals("GBK", encodec)) {
                    response = new String(getMethod.getResponseBody(), "GBK");
                } else {
                    response = new String(getMethod.getResponseBody(), "UTF-8");
                }
                suc ="Y";
            }
        } catch (Throwable t) {
           LogUtil.error(LOGGER,t);
        } finally {

            if (getMethod != null) {
                getMethod.releaseConnection();
            }

            LogUtil.info(LOGGER,"httpGet",url,(System.currentTimeMillis()-timeStart),status,suc);
        }

        return response;
    }

    public static InputStream geInputStream(String urlToRead)  {
        String suc= "N";
        long timeStart = System.currentTimeMillis();
        int status = 0;
        HttpURLConnection conn = null;
        try {

            URL url = new URL(urlToRead);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
            conn.setRequestMethod("GET");
            conn.connect();

            status = conn.getResponseCode();
            suc = "Y";
            return conn.getInputStream();
        }catch (Exception e ){
            if (conn!=null){
                conn.disconnect();
            }
            LogUtil.warn(LOGGER, e, "发送HTTP请求失败");

        }finally {
            LogUtil.info(LOGGER,"geInputStream",urlToRead,(System.currentTimeMillis()-timeStart),status,suc);

        }
        return null;

    }

    public static InputStream httpGetWithStream(String url) {

        return httpGetInputStream(url, null);
    }

    public static InputStream httpGetInputStream(String url, List<Header> headers) {
        InputStream ins = null;
        HttpClient client = getHttpClient();
        GetMethod getMethod = new GetMethod(url);
        String suc= "N";
        long timeStart = System.currentTimeMillis();
        int status = 0;
        try {
            if(!CollectionUtils.isEmpty(headers)) {
                for (Header header : headers) {
                    getMethod.addRequestHeader(header);
                }
            }
            status = client.executeMethod(getMethod);
            //校验返回状态码
            if (status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(getMethod.getResponseBody());
                baos.flush();
                ins = new ByteArrayInputStream(baos.toByteArray());
                suc ="Y";
            }
        } catch (Throwable t) {
            LogUtil.warn(LOGGER, t, "发送HTTP请求失败");
        } finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
            }
            LogUtil.info(LOGGER,"httpGetInputStream",url,(System.currentTimeMillis()-timeStart),status,suc);
        }

        return ins;
    }
}
