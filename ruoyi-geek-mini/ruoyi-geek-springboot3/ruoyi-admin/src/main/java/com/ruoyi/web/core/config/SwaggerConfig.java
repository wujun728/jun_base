package com.ruoyi.web.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;

/**
 * 验证码操作处理
 *
 * @author Dftre
 */
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI springShopOpenApi() {
		Schema<?> codeSchema = new IntegerSchema().example(HttpStatus.SUCCESS); // 示例状态码
		Schema<?> msgSchema = new StringSchema().example("操作成功"); // 示例消息
		ObjectSchema dataSchema = new ObjectSchema(); // 数据可以是任意类型，这里简单定义为ObjectSchema

		// 定义AjaxResult的Schema
		ObjectSchema ajaxResultSchema = new ObjectSchema();
		ajaxResultSchema.addProperty(AjaxResult.CODE_TAG, codeSchema);
		ajaxResultSchema.addProperty(AjaxResult.MSG_TAG, msgSchema);
		ajaxResultSchema.addProperty(AjaxResult.DATA_TAG, dataSchema);
		Components components = new Components();
		components.addSchemas("AjaxResult", ajaxResultSchema);

		return new OpenAPI()
				.components(components)
				.info(new Info().title("RuoYi Geek")
						.description("RuoYi Geek API文档")
						.version("v1")
						.license(new License().name("Apache 2.0").url("http://springdoc.org")))
				.externalDocs(new ExternalDocumentation()
						.description("外部文档")
						.url("/doc.html"));
	}

}
