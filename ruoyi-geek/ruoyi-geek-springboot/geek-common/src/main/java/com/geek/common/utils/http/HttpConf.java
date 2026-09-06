package com.geek.common.utils.http;

/**
 * http 配置信息
 * 
 * @author geek
 */
public class HttpConf {
    /** 获取连接的最大等待时间 */
    public static int WAIT_TIMEOUT = 10000;

    /** 连接超时时间 */
    public static int CONNECT_TIMEOUT = 10000;

    /** 读取超时时间 */
    public static int SO_TIMEOUT = 60000;

    /** 最大连接数 */
    public static int MAX_TOTAL_CONN = 200;

    /** 每个路由最大连接数 */
    public static int MAX_ROUTE_CONN = 150;

    /** 重试次数 */
    public static int RETRY_COUNT = 3;

    /** EPTWebServes地址 */
    public static String EPTWEBSERVES_URL;

    /** tomcat默认keepAliveTimeout为20s */
    public static int KEEP_ALIVE_TIMEOUT;

    private HttpConf() {
    }
}