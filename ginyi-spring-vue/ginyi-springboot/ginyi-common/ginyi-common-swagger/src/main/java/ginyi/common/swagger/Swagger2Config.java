package ginyi.common.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;


public class Swagger2Config {

    /**
     * 标题
     */
    @Value("${swagger.swagger-title}")
    private String title;

    /**
     * 分组
     */
    @Value("${swagger.group-name}")
    private String groupName;

    /**
     * 描述
     */
    @Value("${swagger.swagger-description}")
    private String description;

    /**
     * URL
     */
    @Value("${swagger.swagger-url}")
    private String url;

    /**
     * 作者
     */
    @Value("${swagger.swagger-contact.name}")
    private String contactName;


    /**
     * 作者邮箱
     */
    @Value("${swagger.swagger-contact.email}")
    private String contactEmail;

    /**
     * 版本
     */
    @Value("${swagger.swagger-version}")
    private String version;

    @Bean
    public Docket createAdminApiDoc() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    // 构建 api 文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(url)
                .contact(new Contact(contactName, null, contactEmail))
                .version(version)
                .build();
    }
}
