package com.easymicro.core.https;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpsUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpsUtil.class);

	private static final String HTTP = "http";

	private static final String HTTPS = "https";

	private static SSLConnectionSocketFactory sslsf = null;
	private static PoolingHttpClientConnectionManager cm = null;
	private static SSLContextBuilder builder = null;

	static {
		try {
			builder = new SSLContextBuilder();
			// 全部信任 不做身份鉴定
			builder.loadTrustMaterial(null, (x509Certificates, s) -> true);
			sslsf = new SSLConnectionSocketFactory(builder.build(),
					new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register(HTTP, new PlainConnectionSocketFactory()).register(HTTPS, sslsf).build();
			cm = new PoolingHttpClientConnectionManager(registry);
			cm.setMaxTotal(50);// max connection
		} catch (Exception e) {

		}
	}

	private static CloseableHttpClient getHttpClient() {
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(cm)
				.setConnectionManagerShared(true).build();
		return httpClient;
	}

	/**
	 * 执行https get请求,返回指定对象
	 * 
	 * @param url
	 *            目标url
	 * @param params
	 *            参数
	 * @param respClazz
	 *            期望返回对象
	 */
	public static <T> T executeGet(String url, Map<String, String> params, Class<T> respClazz) {
		T rst = null;
		String rstStr = executeGet(url,params);
		if(respClazz != null && StringUtils.isNotBlank(rstStr)){
			rst = JSONObject.parseObject(rstStr, respClazz);
		}
		return rst;
	}
	
	/**
	 * 执行执行 https get请求,返回指定对象
	 * 
	 * @param url 目标url
	 * @param params 参数
	 * @return 结果以字符串形式返回
	 */
	public static String executeGet(String url, Map<String, String> params){
		CloseableHttpClient httpClient = getHttpClient();
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		if (params != null) {
			params.forEach((k, v) -> nvps.add(new BasicNameValuePair(k, v)));
		}
		String resultString = "";
		try {
			URIBuilder builder = new URIBuilder(new String(url.getBytes(),"utf-8"));
			builder.setParameters(nvps);
			URI requestUri = builder.build();
			HttpGet httpGet = new HttpGet(requestUri);
			logger.info("HttpsUtil发送GET请求处理开始,地址---{},请求参数---{}", requestUri, params);
			CloseableHttpResponse resp = httpClient.execute(httpGet);
			HttpEntity entity = resp.getEntity();
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK && entity != null) {
				resultString = EntityUtils.toString(entity);
				resp.close();
			}
			logger.info("HttpsUtil发送GET请求处理完毕,地址---{},请求参数---{},返回参数---{}", url, params, resultString);
		} catch (ClientProtocolException e) {
			logger.error("HttpsUtil发送GET请求出现异常---{}", e.getMessage());
		} catch (Exception e) {
			logger.error("HttpsUtil发送GET请求出现异常---{}", e.getMessage());
		}
		return resultString;
	}

    /**
     * 执行https POST请求,返回指定的实体对象
     *
     * @param url 请求路径
     * @param params 参数
     * @param clazz 指定返回的实体
     */
	public static <T> T executePost(String url,Map<String,String> params,Class<T> clazz){
        String rstString = executePost(url, null, params, null);
        if (clazz == null) {
            return (T)rstString;
        }
        T t = null;
        if(StringUtils.isNotBlank(rstString)){
            t = JSONObject.parseObject(rstString,clazz);
        }
        return t;
    }

    /**
     * 执行https POST请求
     *
     * @param url 请求路径
     * @param params string类型参数
     */
    public static String executePost(String url,String params){
        HttpEntity entity = new StringEntity(params,"utf-8");
        return executePost(url,null,null,entity);
    }

    /**
     * 执行https POST请求
     *
     * @param url 请求路径
     * @param params string类型参数
     */
    public static String executePost(String url,String params,boolean isCert){
        HttpEntity entity = null;
        entity = new StringEntity(params,"utf-8");
        return executePost(url,null,entity,isCert);
    }

    /**
     * 发送带证书的https post请求
     *
     * @param url 请求url
     * @param param 参数
     * @param entity 实体
     * @param isCert 是否证书加载
     * @return
     */
    public static String executePost(String url, Map<String, String> param,
                                     HttpEntity entity,boolean isCert) {
        String result = "";
        CloseableHttpClient httpClient = null;
        try {
            httpClient = getHttpClient();
            HttpPost httpPost = new HttpPost(url);
            // 设置请求参数
            if (null != param) {
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    // 给参数赋值
                    formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
                httpPost.setEntity(urlEncodedFormEntity);
            }
            // 设置实体 优先级高
            if (entity != null) {
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse httpResponse = null;
            //是否加载证书
            if(isCert){
                //httpResponse = HttpClients.custom().setSSLSocketFactory(CertUtil.initCert()).build().execute(httpPost);
                httpResponse = httpClient.execute(httpPost);
            }else{
                httpResponse = httpClient.execute(httpPost);
            }
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = httpResponse.getEntity();
                result = EntityUtils.toString(resEntity,Consts.UTF_8);
            } else {
                String rst = readHttpResponse(httpResponse);
                throw new RuntimeException(rst);
            }
            httpResponse.close();
        } catch (Exception e) {
            logger.error("HttpsUtil发送POST请求出现异常---{}" , e.getMessage() );
        }
        return result;
    }

	/**
	 * httpClient post请求
	 * 
	 * @param url
	 *            请求url
	 * @param header
	 *            头部信息
	 * @param param
	 *            请求参数 form提交适用
	 * @param entity
	 *            请求实体 json/xml提交适用
	 * @return 可能为空 需要处理
	 *
	 */
	public static String executePost(String url, Map<String, String> header, Map<String, String> param,
			HttpEntity entity) {
		String result = "";
		CloseableHttpClient httpClient = null;
		try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);
			// 设置头信息
			if (null != header) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置请求参数
			if (null != param) {
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : param.entrySet()) {
					// 给参数赋值
					formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
				httpPost.setEntity(urlEncodedFormEntity);
			}
			// 设置实体 优先级高
			if (entity != null) {
				httpPost.setEntity(entity);
			}
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = httpResponse.getEntity();
				result = EntityUtils.toString(resEntity,Consts.UTF_8);
			} else {
				String rst = readHttpResponse(httpResponse);
				throw new RuntimeException(rst);
			}
			httpResponse.close();
		} catch (Exception e) {
			logger.error("HttpsUtil发送POST请求出现异常---{}" , e.getMessage() );
		}
		return result;
	}

	private static String readHttpResponse(HttpResponse httpResponse) throws ParseException, IOException {
		StringBuilder builder = new StringBuilder();
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		// 响应状态
		builder.append("status:" + httpResponse.getStatusLine());
		builder.append("headers:");
		HeaderIterator iterator = httpResponse.headerIterator();
		while (iterator.hasNext()) {
			builder.append("\t" + iterator.next());
		}
		// 判断响应实体是否为空
		if (entity != null) {
			String responseString = EntityUtils.toString(entity);
			builder.append("response length:" + responseString.length());
			builder.append("response content:" + responseString.replace("\r\n", ""));
		}
		return builder.toString();
	}
}
