package ginyi.server.admin.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import ginyi.common.swagger.Swagger2Config;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@EnableKnife4j
@Configuration
public class AdminSwagger2Config extends Swagger2Config {

}
