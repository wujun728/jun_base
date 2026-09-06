package com.ruoyi.framework.sms;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.tea.TeaException;
import com.ruoyi.framework.config.properties.AlismsProperties;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SmsHelper {
    private static final Logger log = LoggerFactory.getLogger(SmsHelper.class);

    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret)
            throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public static com.aliyun.dysmsapi20170525.models.SendSmsResponse sendCaptcha(String phoneNumbers) throws Exception {

        Integer code = RandomUtil.randomInt(100000, 999999);
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.putOpt("code", code);
        // Assuming the first template is the captcha template
        if (AlismsProperties.getTemplates() == null || AlismsProperties.getTemplates().isEmpty()) {
            log.error("No SMS templates configured.");
            return null;
        }
        com.aliyun.dysmsapi20170525.models.SendSmsResponse sendSmsResponse = sendSms(phoneNumbers,
                AlismsProperties.getTemplates().get(0), JSONUtil.toJsonStr(jsonObject));
        if (sendSmsResponse != null && "OK".equals(sendSmsResponse.getBody().getCode())) {
            SpringUtils.getBean(RedisCache.class).setCacheObject("captcha:" + phoneNumbers, code, 5, TimeUnit.MINUTES);
        }
        return sendSmsResponse;
    }

    public static com.aliyun.dysmsapi20170525.models.SendSmsResponse sendSms(String phoneNumbers, String templateCode,
            String templateParam) throws Exception {

        try {
            com.aliyun.dysmsapi20170525.Client client = SmsHelper.createClient(AlismsProperties.getKey(),
                    AlismsProperties.getSecret());
            com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                    .setPhoneNumbers(phoneNumbers)
                    .setSignName(AlismsProperties.getSign())
                    .setTemplateCode(templateCode)
                    .setTemplateParam(templateParam);
            com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
            com.aliyun.dysmsapi20170525.models.SendSmsResponse sendSmsResponse = client
                    .sendSmsWithOptions(sendSmsRequest, runtime);

            if (!"OK".equals(sendSmsResponse.getBody().getCode())) {
                log.error("Failed to send SMS: " + sendSmsResponse.getBody().getMessage());
            }

            return sendSmsResponse;
        } catch (TeaException error) {
            // 错误 message
            System.out.println(error.getMessage());
            log.error("sms-TeaException", error);
            throw error;
        } catch (Exception error) {
            // 错误 message
            System.out.println(error.getMessage());
            log.error("sms-Exception", error);
            throw error;
        }
    }
}
