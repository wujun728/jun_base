package ginyi.server.web;

import ginyi.framework.core.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;
import java.net.UnknownHostException;

@Slf4j
// 扫描系统服务的包以及自身所在模块的包
@SpringBootApplication(scanBasePackages = {
        "ginyi.system",
        "ginyi.common",
        "ginyi.framework.websocket",
        "ginyi.framework.security",
        "ginyi.server.web"
})
@MapperScan({
        "ginyi.system.**.mapper",
        "ginyi.server.admin.**.mapper"
})
public class WebApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext context = SpringApplication.run(WebApplication.class, args);
        AppConfig.printAppInfo(context);
    }
}
