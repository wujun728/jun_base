package com.ruoyi.modelMessage.utils;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.modelMessage.annotation.MessageLog;
import com.ruoyi.modelMessage.domain.MessageSystem;
import com.ruoyi.modelMessage.enums.MessageType;
import com.ruoyi.modelMessage.service.IMessageSystemService;

@Aspect
@Component
@Order(1) //Aspect 的优先级
public class MessageLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(MessageLogAspect.class);

    @Autowired
    private IMessageSystemService messageSystemService;

    @Around("@annotation(messageLog)")
    public Object handleMessageLog(ProceedingJoinPoint joinPoint, MessageLog messageLog) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取注解信息
        String description = messageLog.description();
        String title = messageLog.title();
        MessageType messageType = messageLog.messageType();
        boolean immediateLog = messageLog.immediateLog();
        try {
            Object result = joinPoint.proceed();
            // 如果设置了true立即执行
            if (immediateLog) {
                logMessage(title, description, messageType);
            }
            return result;
        } catch (Exception e) {
            logger.error("消息记录失败，方法: {}, 描述: {}", method.getName(), description, e);
            throw e;
        }
    }

    private void logMessage(String title, String description, MessageType messageType) {
        MessageSystem messageSystem = new MessageSystem();
        messageSystem.setMessageTitle(title); // 标题
        messageSystem.setCreateBy(SecurityUtils.getUsername()); // 发送人
        messageSystem.setCreateTime(DateUtils.getNowDate()); // 发送时间
        messageSystem.setMessageContent(description); // 信息内容
        messageSystem.setMessageStatus("0"); // 默认为未读 0未读 1 已读
        messageSystem.setMessageType(messageType.getCode()); 
        messageSystem.setUpdateBy(SecurityUtils.getUsername()); // 修改人
        messageSystem.setUpdateTime(DateUtils.getNowDate()); // 修改时间
        messageSystem.setSendMode("0"); // 默认发送方式为平台
        messageSystemService.insertMessageSystem(messageSystem);
        logger.info("消息记录成功，标题: {}, 描述: {}, 类型: {}", title, description, messageType);
    }
}
