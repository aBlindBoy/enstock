package com.xc.utils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;


public class HttpClientRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpClientRequest.class);

    public static String doGet(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";

        try {
            httpClient = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(url);

//            httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
//            httpGet.setHeader("Referer", "http://finance.sina.com.cn");
//            .setProxy(httpHost)
//            SSLContext sslcontext = SSLContexts.custom()
//                    .loadTrustMaterial(new File("/opt/my.store"), "123456".toCharArray(),
//                            new TrustSelfSignedStrategy()).build();

//            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//                    sslcontext,
//                    new String[]{"TLSv1"},
//                    null,
//                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
//            HttpsUtil.trustEveryone();
//            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            HttpHost httpHost = new HttpHost("127.0.0.1",7890);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10*1000).
                    setConnectionRequestTimeout(10*1000).setSocketTimeout(10*1000)
                    .setAuthenticationEnabled(false)
                    .setProxy(httpHost)
                    .build();
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            log.error("????????????", e);
        } catch (IOException e) {
            log.error("io??????", e);
        } finally {

            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("io??????", e);
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("io??????", e);
                }
            }
        }
        return result;
    }

    public static String doPost(String url, Map<String, Object> paramMap) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";

        httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);


        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();

        httpPost.setConfig(requestConfig);

        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

        if (null != paramMap && paramMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();

            Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();

            Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> mapEntry = (Map.Entry) iterator.next();
                nvps.add(new BasicNameValuePair((String) mapEntry.getKey(), mapEntry.getValue().toString()));
            }

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("??????", e);
            }
        }

        try {
            httpResponse = httpClient.execute(httpPost);

            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            log.error("io??????", e);
        } catch (IOException e) {
            log.error("io??????", e);
        } finally {

            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    log.error("io??????", e);
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("io??????", e);
                }
            }
        }
        return result;
    }
}
