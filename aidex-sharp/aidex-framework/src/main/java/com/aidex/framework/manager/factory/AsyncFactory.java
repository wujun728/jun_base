package com.aidex.framework.manager.factory;

import com.aidex.common.constant.Constants;
import com.aidex.common.utils.LogUtils;
import com.aidex.common.utils.ServletUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.common.utils.ip.AddressUtils;
import com.aidex.common.utils.ip.IpUtils;
import com.aidex.common.utils.spring.SpringUtils;
import com.aidex.system.domain.SysLoginLog;
import com.aidex.system.domain.SysOperLog;
import com.aidex.system.service.SysLoginLogService;
import com.aidex.system.service.SysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 */
public class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
                                             final Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLoginLog sysLoginLog = new SysLoginLog();
                sysLoginLog.setUserName(username);
                sysLoginLog.setIpaddr(ip);
                sysLoginLog.setLoginLocation(address);
                sysLoginLog.setBrowser(browser);
                sysLoginLog.setOs(os);
                sysLoginLog.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
                    sysLoginLog.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    sysLoginLog.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(SysLoginLogService.class).addLoginLog(sysLoginLog);
            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(SysOperLogService.class).addOperlog(operLog);
            }
        };
    }
}
