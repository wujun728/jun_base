package ginyi.common.utils;

import cn.hutool.extra.mail.MailUtil;

/**
 * 邮箱工具类
 * 如何使用：
 *   1、需要在对应的应用服务模块下（如ginyi-server-admin）的resource目录下，新建config目录，再新建mail.setting配置文件
 *   2、具体配置信息参考 https://www.hutool.cn/docs/#/extra/邮件工具-MailUtil
 */
public class EmailUtils {

    /**
     * 单点发送
     * @param to 目标
     * @param subject 标题
     * @param message 信息
     * @param isHtml 是否为html信息
     * @return
     */
    public static boolean sendToOne(String to, String subject, String message, Boolean isHtml) {
        String result = MailUtil.send(to, subject, message, isHtml);
        return result != null;
    }
}
