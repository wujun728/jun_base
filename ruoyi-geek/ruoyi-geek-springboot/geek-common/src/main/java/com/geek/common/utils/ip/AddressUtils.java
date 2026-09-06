package com.geek.common.utils.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geek.common.config.GeekConfig;
import com.geek.common.utils.JSON;
import com.geek.common.utils.StringUtils;
import com.geek.common.utils.http.HttpUtils;

import tools.jackson.databind.JsonNode;

/**
 * 获取地址类
 * 
 * @author geek
 */
public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    /** IP地址查询 */
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    /** 未知地址 */
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        if (GeekConfig.isAddressEnabled()) {
            try {
                String rspStr = HttpUtils.get(IP_URL + "?ip=" + ip + "&json=true");
                if (StringUtils.isEmpty(rspStr)) {
                    log.error("获取地理位置异常 {}", ip);
                    return UNKNOWN;
                }
                JsonNode obj = JSON.parseObject(rspStr);
                String region = obj.get("pro").asString();
                String city = obj.get("city").asString();
                return String.format("%s %s", region, city);
            } catch (Exception e) {
                log.error("获取地理位置异常 {}", ip);
            }
        }
        return UNKNOWN;
    }

    private AddressUtils() {
    }
}
