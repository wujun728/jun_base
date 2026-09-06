package com.ruoyi.client.annotation.column.props;

import com.ruoyi.client.annotation.AVueAttr;
import com.ruoyi.client.annotation.AVueRule;

import java.lang.annotation.*;

/**
 * 描述: 级联选择器
 *
 * @author liukx
 * @date 2021/8/16 18:51
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AVueUploadPropsHttp {

    /**
     * 图片的根路径地址，例如返回data:{url:' / xxxx.jpg',name:''},home属性为http://xxx.com/,则最终的图片显示地址为http://xxx.com/xxxx.jpg
     *
     * @return
     */
    String home() default "";

    /**
     * 返回结构体的层次，例如返回data:{url:'',name:''},则res配置为data
     *
     * @return
     */
    String res();

    /**
     * 上传成功返回结构体的图片地址，例如返回data:{urlsrc:'',name:''},则url配置为urlsrc
     *
     * @return
     */
    String url() default "";

    /**
     * 上传成功返回结构体的图片名称，例如返回data:{urlsrc:'',namesrc:''},则name配置为namesrc，当listType为picture-img属性不存在
     *
     * @return
     */
    String name();

    /**
     * 上传文件流时的名称
     *
     * @return
     */
    String fileName() default "";

}
