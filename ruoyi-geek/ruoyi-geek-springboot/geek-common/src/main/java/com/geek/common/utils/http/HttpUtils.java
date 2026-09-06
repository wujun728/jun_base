package com.geek.common.utils.http;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.ConnectTimeoutException;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.config.RequestConfig.Builder;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geek.common.constant.Constants;
import com.geek.common.utils.JSON;
import com.geek.common.utils.StringUtils;

/**
 * 通用http发送方法
 * 
 * @author geek
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public static RequestConfig requestConfig;

    private static CloseableHttpClient httpClient;

    private static PoolingHttpClientConnectionManager connMgr;

    static {
        HttpUtils.initClient();
    }

    // ================= 新统一 API =================
    public static String get(String url) {
        try {
            return getCall(url, null, Constants.UTF8);
        } catch (Exception e) {
            log.error("GET 请求异常 url={}", url, e);
            return null;
        }
    }

    public static String get(String url, Map<String, String> headers) {
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            if (headers != null)
                headers.forEach(httpGet::addHeader);
            return execute(httpGet, Constants.UTF8, true);
        } catch (Exception e) {
            log.error("GET 请求异常", e);
            return null;
        }
    }

    public static String postJson(String url, Object body) {
        try {
            return postCall(url, ContentType.APPLICATION_JSON.getMimeType(), JSON.toJSONString(body));
        } catch (Exception e) {
            log.error("POST JSON 异常", e);
            return null;
        }
    }

    /**
     * POST JSON（支持自定义请求头）
     */
    public static String postJson(String url, Object body, Map<String, String> headers) {
        try {
            return postWithHeaders(url, ContentType.APPLICATION_JSON.getMimeType(), JSON.toJSONString(body), headers);
        } catch (Exception e) {
            log.error("POST JSON 异常", e);
            return null;
        }
    }

    public static String postForm(String url, Map<String, Object> form) {
        try {
            return postCall(url, "application/x-www-form-urlencoded", form);
        } catch (Exception e) {
            log.error("POST Form 异常", e);
            return null;
        }
    }

    public static String postXml(String url, String xml) {
        try {
            return postCall(url, ContentType.APPLICATION_XML.getMimeType(), xml);
        } catch (Exception e) {
            log.error("POST XML 异常", e);
            return null;
        }
    }

    public static String postRaw(String url, String raw, ContentType type) {
        try {
            return postCall(url, type != null ? type.getMimeType() : "text/plain", raw);
        } catch (Exception e) {
            log.error("POST Raw 异常", e);
            return null;
        }
    }

    public static String postRaw(String url, String raw, ContentType type, Map<String, String> headers) {
        try {
            return postWithHeaders(url, type != null ? type.getMimeType() : "text/plain", raw, headers);
        } catch (Exception e) {
            log.error("POST Raw 异常", e);
            return null;
        }
    }

    /**
     * 获取httpClient
     * 
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        if (httpClient != null) {
            return httpClient;
        } else {
            return HttpClients.createDefault();
        }
    }

    /**
     * 创建连接池管理器
     * 
     * @return
     */
    private static PoolingHttpClientConnectionManager createConnectionManager() {

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();
        // 将最大连接数增加到
        connMgr.setMaxTotal(HttpConf.MAX_TOTAL_CONN);
        // 将每个路由基础的连接增加到
        connMgr.setDefaultMaxPerRoute(HttpConf.MAX_ROUTE_CONN);

        return connMgr;
    }

    /**
     * 根据当前配置创建HTTP请求配置参数。
     * 
     * @return 返回HTTP请求配置。
     */
    @SuppressWarnings("deprecation")
    private static RequestConfig createRequestConfig() {
        Builder builder = RequestConfig.custom();
        int crTimeout = StringUtils.nvl(HttpConf.WAIT_TIMEOUT, 10000);
        int connTimeout = StringUtils.nvl(HttpConf.CONNECT_TIMEOUT, 10000);
        int respTimeout = StringUtils.nvl(HttpConf.SO_TIMEOUT, 60000);
        builder.setConnectionRequestTimeout(Timeout.ofMilliseconds(crTimeout));
        builder.setConnectTimeout(Timeout.ofMilliseconds(connTimeout));
        builder.setResponseTimeout(Timeout.ofMilliseconds(respTimeout));
        return builder.build();
    }

    /**
     * 创建默认的HTTPS客户端，信任所有的证书。
     * 
     * @return 返回HTTPS客户端，如果创建失败，返回HTTP客户端。
     */
    private static CloseableHttpClient createHttpClient(PoolingHttpClientConnectionManager connMgr) {
        try {
            // 构建信任所有证书的上下文（不鼓励生产使用）
            SSLContextBuilder.create().loadTrustMaterial(null, (chain, authType) -> true).build();
            ConnectionKeepAliveStrategy connectionKeepAliveStrategy = (response, context) -> TimeValue
                    .ofMilliseconds(HttpConf.KEEP_ALIVE_TIMEOUT);
            DefaultHttpRequestRetryStrategy retryStrategy = new DefaultHttpRequestRetryStrategy(HttpConf.RETRY_COUNT,
                    TimeValue.ofMilliseconds(1000));
            httpClient = HttpClients.custom()
                    // 直接设置 SSLContext 支持（5.x 没有 setSSLContext 方法时，依赖默认）
                    .setConnectionManager(connMgr)
                    .setDefaultRequestConfig(requestConfig)
                    .setRetryStrategy(retryStrategy)
                    .setKeepAliveStrategy(connectionKeepAliveStrategy)
                    .evictExpiredConnections()
                    .evictIdleConnections(TimeValue.ofMilliseconds(HttpConf.KEEP_ALIVE_TIMEOUT))
                    .build();
        } catch (Exception e) {
            log.error("Create http client failed", e);
            httpClient = HttpClients.createDefault();
        }

        return httpClient;
    }

    /**
     * 初始化 只需调用一次
     */
    public synchronized static CloseableHttpClient initClient() {
        if (httpClient == null) {
            connMgr = createConnectionManager();
            requestConfig = createRequestConfig();
            // 初始化httpClient连接池
            httpClient = createHttpClient(connMgr);
            // 使用内置 evict 机制，无需独立线程
        }

        return httpClient;
    }

    /**
     * 关闭HTTP客户端。
     * 
     * @param httpClient HTTP客户端。
     */
    public synchronized static void shutdown() {
        try {
            // no extra thread to shutdown
        } catch (Exception e) {
            log.error("httpclient connection manager close", e);
        }

        try {
            if (httpClient != null) {
                httpClient.close();
                httpClient = null;
            }
        } catch (IOException e) {
            log.error("httpclient close", e);
        }
    }

    /**
     * 请求上游 GET提交
     * 
     * @param uri
     * @throws IOException
     */
    public static String getCall(final String uri) throws Exception {

        return getCall(uri, null, Constants.UTF8);
    }

    /**
     * 请求上游 GET提交
     * 
     * @param uri
     * @param contentType
     * @throws IOException
     */
    public static String getCall(final String uri, String contentType) throws Exception {

        return getCall(uri, contentType, Constants.UTF8);
    }

    /**
     * 请求上游 GET提交
     * 
     * @param uri
     * @param contentType
     * @param charsetName
     * @throws IOException
     */
    public static String getCall(final String uri, String contentType, String charsetName) throws Exception {

        final String url = uri;
        final HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        if (!StringUtils.isEmpty(contentType)) {
            httpGet.addHeader("Content-Type", contentType);
        }
        return execute(httpGet, charsetName, true);

    }

    /**
     * 请求上游 POST提交
     * 
     * @param uri
     * @param paramsMap
     * @throws IOException
     */
    public static String postCall(final String uri, Map<String, Object> paramsMap) throws Exception {
        return postCall(uri, null, paramsMap, Constants.UTF8);
    }

    /**
     * 请求上游 POST提交
     * 
     * @param uri
     * @param contentType
     * @param paramsMap
     * @throws IOException
     */
    public static String postCall(final String uri, String contentType, Map<String, Object> paramsMap)
            throws Exception {

        return postCall(uri, contentType, paramsMap, Constants.UTF8);
    }

    /**
     * 请求上游 POST提交
     * 
     * @param uri
     * @param contentType
     * @param paramsMap
     * @param charsetName
     * @throws IOException
     */
    public static String postCall(final String uri, String contentType, Map<String, Object> paramsMap,
            String charsetName) throws Exception {

        final String url = uri;
        final HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if (!StringUtils.isEmpty(contentType)) {
            httpPost.addHeader("Content-Type", contentType);
        }
        // 添加参数
        List<NameValuePair> list = new ArrayList<>();
        if (paramsMap != null) {
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), (String) entry.getValue()));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(list, Charset.forName(charsetName)));

        return execute(httpPost, charsetName, false);
    }

    /**
     * 请求上游 POST提交
     * 
     * @param uri
     * @param param
     * @throws IOException
     */
    public static String postCall(final String uri, String param) throws Exception {

        return postCall(uri, null, param, Constants.UTF8);
    }

    /**
     * 请求上游 POST提交
     * 
     * @param uri
     * @param contentType
     * @param param
     * @throws IOException
     */
    public static String postCall(final String uri, String contentType, String param) throws Exception {

        return postCall(uri, contentType, param, Constants.UTF8);
    }

    /**
     * 请求上游 POST提交
     * 
     * @param uri
     * @param contentType
     * @param param
     * @param charsetName
     * @throws IOException
     */
    public static String postCall(final String uri, String contentType, String param, String charsetName)
            throws Exception {

        final String url = uri;
        final HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if (!StringUtils.isEmpty(contentType)) {
            httpPost.addHeader("Content-Type", contentType);
        } else {
            httpPost.addHeader("Content-Type", "application/json");
        }
        // 添加参数
        // 兼容 5.x API 使用 ContentType 指定字符集
        StringEntity paramEntity = new StringEntity(param,
                ContentType.parse(contentType != null ? contentType : "application/json")
                        .withCharset(Charset.forName(charsetName)));
        httpPost.setEntity(paramEntity);

        return execute(httpPost, charsetName, false);
    }

    /**
     * 判断HTTP异常是否为读取超时。
     * 
     * @param e 异常对象。
     * @return 如果是读取引起的异常（而非连接），则返回true；否则返回false。
     */
    public static boolean isReadTimeout(final Throwable e) {
        return (!isCausedBy(e, ConnectTimeoutException.class) && isCausedBy(e, SocketTimeoutException.class));
    }

    /**
     * 检测异常e被触发的原因是不是因为异常cause。检测被封装的异常。
     * 
     * @param e     捕获的异常。
     * @param cause 异常触发原因。
     * @return 如果异常e是由cause类异常触发，则返回true；否则返回false。
     */
    public static boolean isCausedBy(final Throwable e, final Class<? extends Throwable> cause) {
        if (cause.isAssignableFrom(e.getClass())) {
            return true;
        } else {
            Throwable t = e.getCause();
            while (t != null && t != e) {
                if (cause.isAssignableFrom(t.getClass())) {
                    return true;
                }
                t = t.getCause();
            }
            return false;
        }
    }

    private static String execute(HttpUriRequestBase request, String charset, boolean allowForbidden)
            throws IOException {
        HttpClientResponseHandler<String> handler = response -> {
            int code = response.getCode();
            if (code == HttpStatus.SC_OK || (allowForbidden && code == HttpStatus.SC_FORBIDDEN)) {
                HttpEntity entity = response.getEntity();
                String text = entity != null ? EntityUtils.toString(entity, charset) : "";
                if (entity != null)
                    EntityUtils.consume(entity);
                return text;
            }
            throw new IOException("HTTP StatusCode=" + code);
        };
        return getHttpClient().execute(request, handler);
    }

    private static String postWithHeaders(String url, String contentType, String body, Map<String, String> headers)
            throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if (contentType != null) {
            httpPost.addHeader("Content-Type", contentType);
        }
        if (headers != null) {
            headers.forEach((k, v) -> {
                if (v != null)
                    httpPost.addHeader(k, v);
            });
        }
        StringEntity entity = new StringEntity(body == null ? "" : body,
                ContentType.parse(contentType != null ? contentType : "text/plain")
                        .withCharset(Charset.forName(Constants.UTF8)));
        httpPost.setEntity(entity);
        return execute(httpPost, Constants.UTF8, false);
    }

    private HttpUtils() {
    }
}