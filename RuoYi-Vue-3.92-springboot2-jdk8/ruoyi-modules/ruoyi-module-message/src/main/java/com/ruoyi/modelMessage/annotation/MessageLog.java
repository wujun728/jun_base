package com.ruoyi.modelMessage.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ruoyi.modelMessage.enums.MessageType;


//自定义消息系统注解
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageLog {
    /**
     * 消息操作的描述
     */
    String description() default "";

    /**
     * 消息标题
     */
    String title() default "";

    /**
     * 是否立即记录消息，默认为 true
     */
    boolean immediateLog() default true;

    /**
     * 操作类型
     */
    MessageType messageType() default MessageType.INFO;
}
