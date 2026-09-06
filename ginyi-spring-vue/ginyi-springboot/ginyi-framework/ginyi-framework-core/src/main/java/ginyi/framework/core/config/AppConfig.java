package ginyi.framework.core.config;

import lombok.extern.slf4j.Slf4j;
import org.fusesource.jansi.Ansi;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class AppConfig {

    /**
     * 彩色打印字体
     */
    public static String colorPrint(String s, Ansi.Color color) {
        return Ansi.ansi().eraseScreen().fg(color).a(s).reset().toString();
    }

    public static void printAppInfo(ConfigurableApplicationContext context) throws UnknownHostException {
        // 获取环境配置信息
        ConfigurableEnvironment env = context.getEnvironment();
        String projectName = env.getProperty("ginyi.project-name");
        String contextPath = env.getProperty("server.servlet.context-path");
        String port = env.getProperty("server.port");
        String baseUrl = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port + contextPath;
        String swaggerDoc = baseUrl + "swagger-ui.html";
        String knife4jDoc = baseUrl + "doc.html";

        String startSuccess = "\n" +
                "  _______  __   __   __  ____    ____  __     \n" +
                " /  _____||  | |  \\ |  | \\   \\  /   / |  | 项目名称：" + projectName + "\n" +
                "|  |  __  |  | |   \\|  |  \\   \\/   /  |  | 服务地址：" + baseUrl + "\n" +
                "|  | |_ | |  | |       |   \\_    _/   |  | 接口文档：" + knife4jDoc + " or " + swaggerDoc + "\n" +
                "|  |__| | |  | |  |\\   |     |  |     |  | 如果你喜欢这个项目，欢迎Star！https://gitee.com/Ginyi/ginyi-spring-vue \n" +
                " \\______| |__| |__| \\__|     |__|     |__| 启动成功...\n";

        log.info("\n{}", colorPrint(startSuccess, Ansi.Color.CYAN));
    }

}
