package com.ruoyi.pay.sqb.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "pay.sqb", name = "enabled", havingValue = "true")
public class SqbConfig {
    @Value("${pay.sqb.apiDomain}")
    private String apiDomain;

    @Value("${pay.sqb.terminalSn}")
    private String terminalSn;

    @Value("${pay.sqb.terminalKey}")
    private String terminalKey;

    @Value("${pay.sqb.appId}")
    private String appId;

    @Value("${pay.sqb.vendorSn}")
    private String vendorSn;

    @Value("${pay.sqb.vendorKey}")
    private String vendorKey;

    @Value("${pay.sqb.notifyUrl}")
    private String notifyUrl;

    @Value("${pay.sqb.publicKey}")
    private String publicKey;

    @Autowired
    private ApplicationContext applicationContext;

    public String getPublicKey() throws Exception {
        if (publicKey.startsWith("classpath")) {
            Resource resource = applicationContext.getResource(publicKey);
            InputStream inputStream = resource.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String alipayPublicKeyValue = bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
            bufferedReader.close();
            publicKey = alipayPublicKeyValue;
        }
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getApiDomain() {
        return apiDomain;
    }

    public void setApiDomain(String apiDomain) {
        this.apiDomain = apiDomain;
    }

    public String getTerminalSn() {
        return terminalSn;
    }

    public void setTerminalSn(String terminalSn) {
        this.terminalSn = terminalSn;
    }

    public String getTerminalKey() {
        return terminalKey;
    }

    public void setTerminalKey(String terminalKey) {
        this.terminalKey = terminalKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVendorSn() {
        return vendorSn;
    }

    public void setVendorSn(String vendorSn) {
        this.vendorSn = vendorSn;
    }

    public String getVendorKey() {
        return vendorKey;
    }

    public void setVendorKey(String vendorKey) {
        this.vendorKey = vendorKey;
    }
}
