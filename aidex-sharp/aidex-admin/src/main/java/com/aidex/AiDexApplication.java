package com.aidex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class AiDexApplication
{
    public static void main(String[] args)
    {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(AiDexApplication.class, args);
        StringBuffer aidex = new StringBuffer("(♥◠‿◠)ﾉﾞ  AiDex启动成功   ლ(´ڡ`ლ)ﾞ  \n");
        aidex.append("    /\\   (_)  __ \\             / ____| |\n");
        aidex.append("   /  \\   _| |  | | _____  __ | (___ | |__   __ _ _ __ _ __\n");
        aidex.append("  / /\\ \\ | | |  | |/ _ \\ \\/ /  \\___ \\| '_ \\ / _` | '__| '_ \\\n");
        aidex.append(" / ____ \\| | |__| |  __/>  <   ____) | | | | (_| | |  | |_) |\n");
        aidex.append("/_/    \\_\\_|_____/ \\___/_/\\_\\ |_____/|_| |_|\\__,_|_|  | .__/\n");
        aidex.append("                                                      | |\n");
        aidex.append("                                                      |_|");
        System.out.println(aidex);
    }
}
