package com.ruoyi.client;

import com.ruoyi.client.annotation.EnableAVue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 测试入口
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/18 - 15:06
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAVue(basePackages = {"com.ruoyi.client.model"},enumsPackages = "com.ruoyi.client.enums")
public class RuoYiClientTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RuoYiClientTestApplication.class, args);
    }
}
