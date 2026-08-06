package com.ruoyi.pay.alipay.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;

/**
 * @author zlh
 */
@Configuration
@ConditionalOnProperty(prefix = "pay.alipay", name = "enabled", havingValue = "true")
public class AliPayConfig {
    @Value("${pay.alipay.appId}")
    private String appId;
    @Value("${pay.alipay.notifyUrl}")
    private String notifyUrl;
    @Value("${pay.alipay.appPrivateKey:}")
    private String appPrivateKey;
    @Value("${pay.alipay.alipayPublicKey:}")
    private String alipayPublicKey;
    @Value("${pay.alipay.signType:RSA2}")
    private String signType;
    @Value("${pay.alipay.merchantCertPath:}")
    private String merchantCertPath;
    @Value("${pay.alipay.alipayCertPath:}")
    private String alipayCertPath;
    @Value("${pay.alipay.alipayRootCertPath:}")
    private String alipayRootCertPath;
    @Value("${pay.alipay.gatewayHost:openapi.alipay.com}")
    private String gatewayHost;
    @Value("${pay.alipay.protocol:https}")
    private String protocol;

    @Autowired
    private ApplicationContext applicationContext;

    // 强制用UTF-8读取密钥文件，无论JVM file.encoding如何，保证验签一致
    private String getAppPrivateKey() throws Exception {
        if (appPrivateKey.startsWith("classpath")) {
            Resource resource = applicationContext.getResource(appPrivateKey);
            try (InputStream inputStream = resource.getInputStream()) {
                // 密钥文件必须为UTF-8编码
                return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            }
        }
        return appPrivateKey;
    }

    // 强制用UTF-8读取密钥文件，无论JVM file.encoding如何，保证验签一致
    private String getAlipayPublicKey() throws Exception {
        if (alipayPublicKey.startsWith("classpath")) {
            Resource resource = applicationContext.getResource(alipayPublicKey);
            try (InputStream inputStream = resource.getInputStream()) {
                // 密钥文件必须为UTF-8编码
                return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            }
        }
        return alipayPublicKey;
    }

    @Bean
    protected Config alipayBaseConfig() throws Exception {
        // 设置参数（全局只需设置一次）
        Config config = new Config();
        config.protocol = protocol;
        config.gatewayHost = gatewayHost;// openapi-sandbox.dl.alipaydev.com||openapi.alipay.com
        config.signType = signType;
        config.appId = appId;
        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = getAppPrivateKey();
        // 注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
        // 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2019051064521003.crt
        config.merchantCertPath = merchantCertPath;
        // 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt
        config.alipayCertPath = alipayCertPath;
        // 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt
        config.alipayRootCertPath = alipayRootCertPath;
        // 注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = getAlipayPublicKey();
        config.notifyUrl = this.notifyUrl;
        Factory.setOptions(config);
        return config;
    }

}

// https://openapi-sandbox.dl.alipaydev.com/gateway.do