package com.aidex.quartz.task;

import com.aidex.quartz.service.ISysJobLogService;
import com.aidex.system.service.SysLoginLogService;
import com.aidex.system.service.SysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时清理日志定时器
 */
@Component("systemRemoveLogTask")
public class SystemRemoveLogTask
{
    @Autowired
    private SysOperLogService operLogService;

    @Autowired
    private SysLoginLogService sysLoginLogService;

    @Autowired
    private ISysJobLogService jobLogService;
    /**
     * 清除日志数据
     * @param saveDay 日志保存多少天
     * @return
     */
    public String removeLog(String saveDay){
        String message = "";
        try{
            Long startTime = System.currentTimeMillis();
            operLogService.deleteOperLogBySaveDay(saveDay);
            Long endTime = System.currentTimeMillis();
            message = "清除操作日志成功耗时：（"+(endTime-startTime)+"）";
            sysLoginLogService.deleteLoginLogBySaveDay(saveDay);
            Long endTime1 = System.currentTimeMillis();
            message += "清除登录日志成功耗时：（"+(endTime1-endTime)+"）";
            jobLogService.deleteJobLogBySaveDay(saveDay);
            Long endTime2 = System.currentTimeMillis();
            message += "清除调度日志成功耗时：（"+(endTime2-endTime1)+"）";
        }catch (Exception e){
            message = e.getLocalizedMessage();
        }
        return message;
    }
}
