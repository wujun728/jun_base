package com.ruoyi.oss.client;

import com.ruoyi.oss.constant.Constants;

/**
 * 文件上传Factory
 */
public final class OSSClientFactory {

    public static IOSSClient build(String clientType) {
        IOSSClient ossClient;
        if (Constants.CLIENT_LOCAL.equals(clientType)) {
            ossClient = new LocalClient();
        } else if (Constants.CLIENTA_ALIYUN.equals(clientType)) {
            ossClient = new AliyunOSSClient();
        } else {
            return null;
        }
        return ossClient;
    }

}
