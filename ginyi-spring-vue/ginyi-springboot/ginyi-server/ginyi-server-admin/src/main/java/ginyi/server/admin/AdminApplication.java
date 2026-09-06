package ginyi.server.admin;


import ginyi.framework.core.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.UnknownHostException;

// 扫描系统服务的包以及自身所在模块的包
@SpringBootApplication(scanBasePackages = {
        "ginyi.system",
        "ginyi.common",
        "ginyi.framework.security",
        "ginyi.server.admin"
})
@MapperScan({
        "ginyi.system.**.mapper",
        "ginyi.server.admin.**.mapper"
})
@Slf4j
public class AdminApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext context = SpringApplication.run(AdminApplication.class, args);
        AppConfig.printAppInfo(context);
    }
}
