package com.ruoyi.compat.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc API文档配置
 * 用于兼容OA模块的Swagger注解
 *
 * @author ruoyi
 */
@Configuration
public class SpringDocCompatConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RuoYi-Fast OA API")
                        .version("5.2.0")
                        .description("RuoYi-Fast OA模块API文档")
                        .contact(new Contact()
                                .name("RuoYi")
                                .url("https://gitee.com/zccbbg/wms-ruoyi")));
    }
}
