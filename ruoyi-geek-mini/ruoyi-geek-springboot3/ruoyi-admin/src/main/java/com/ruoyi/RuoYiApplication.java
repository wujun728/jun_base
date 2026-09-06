package com.ruoyi;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication {
    public static void main(String[] args) throws UnknownHostException {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        ConfigurableApplicationContext application = SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  若依极客启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'     " + " ____           _         " + "\n" +
                " |  |\\ \\  |  ||   |(_,_)'    " + "  / ___| ___  ___| | __   " + "\n" +
                " |  | \\ `'   /|   `-'  /      " + "| |  _ / _ \\/ _ \\ |/ /  " + "\n" +
                " |  |  \\    /  \\      /      " + " | |_| |  __/  __/   <    " + "\n" +
                " ''-'   `'-'    `-..-'         " + "\\____|\\___|\\___|_|\\_\\");

        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        System.out.println("\n----------------------------------------------------------\n" +
                " Application Ruoyi-Geek is running! Access URLs:\n" +
                " Local:        http://localhost:" + port + "/\n" +
                " External:     http://" + ip + ":" + port + "/\n" +
                " Swagger文档:  http://" + ip + ":" + port + "/swagger-ui/index.html\n" +
                " Knife4j文档:  http://" + ip + ":" + port + "/doc.html" + "" + "\n" +
                "----------------------------------------------------------");
    }
}
