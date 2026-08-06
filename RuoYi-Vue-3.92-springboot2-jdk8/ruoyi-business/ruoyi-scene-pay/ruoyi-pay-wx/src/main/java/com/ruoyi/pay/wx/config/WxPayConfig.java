package com.ruoyi.pay.wx.config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.refund.RefundService;

/**
 * 配置我们自己的信息
 *
 * @author ZlH
 */
@Configuration
@ConditionalOnProperty(prefix = "pay.wechat", name = "enabled", havingValue = "true")
public class WxPayConfig {

    /** 商户号 */
    @Value("${pay.wechat.merchantId}")
    private String merchantId;

    /** 商户证书序列号 */
    @Value("${pay.wechat.merchantSerialNumber}")
    private String merchantSerialNumber;

    /** 商户APIV3密钥 */
    @Value("${pay.wechat.apiV3Key}")
    private String apiV3Key;

    /** 商户API私钥路径 */
    @Value("${pay.wechat.privateKeyPath}")
    private String privateKeyPath;

    @Value("${pay.wechat.appId}")
    private String appId;

    @Value("${pay.wechat.notifyUrl}")
    private String notifyUrl;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantSerialNumber() {
        return merchantSerialNumber;
    }

    public void setMerchantSerialNumber(String merchantSerialNumber) {
        this.merchantSerialNumber = merchantSerialNumber;
    }

    public String getApiV3Key() {
        return apiV3Key;
    }

    public void setApiV3Key(String apiV3Key) {
        this.apiV3Key = apiV3Key;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @Bean
    public RSAAutoCertificateConfig wxpayBaseConfig() throws Exception {
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(getMerchantId())
                .privateKeyFromPath(getPrivateKeyPath())
                .merchantSerialNumber(getMerchantSerialNumber())
                .apiV3Key(getApiV3Key())
                .build();
    }

    @Bean
    public NativePayService nativePayService() throws Exception {
        return new NativePayService.Builder().config(wxpayBaseConfig()).build();
    }

    @Bean
    public RefundService refundService() throws Exception {
        return new RefundService.Builder().config(wxpayBaseConfig()).build();
    }

    @Bean
    public NotificationParser notificationParser() throws Exception {
        return new NotificationParser(wxpayBaseConfig());
    }

    @Autowired
    private ApplicationContext applicationContext;

    public String getPrivateKeyPath() throws Exception {
        if (privateKeyPath.startsWith("classpath:")) {
            Resource resource = applicationContext.getResource(privateKeyPath);
            String tempFilePath = System.getProperty("java.io.tmpdir") + "/temp_wxcert.pem";
            try (InputStream inputStream = resource.getInputStream()) {
                Files.copy(inputStream, Paths.get(tempFilePath), StandardCopyOption.REPLACE_EXISTING);
                privateKeyPath = tempFilePath;
            } catch (Exception e) {
                Files.deleteIfExists(Paths.get(tempFilePath));
                throw new RuntimeException("微信支付证书文件读取失败", e);
            }
        }
        return privateKeyPath;
    }

}
