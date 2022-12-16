package com.mobiloitte.notification.config;

import springfox.documentation.swagger2.annotations.*;
import springfox.documentation.spring.web.plugins.*;
import springfox.documentation.spi.*;
import springfox.documentation.builders.*;
import com.google.common.base.*;
import org.springframework.context.annotation.*;
import springfox.documentation.service.*;
import java.util.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(Predicates.not(PathSelectors.regex("/error.*"))).build().apiInfo(this.apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Notification Service", "API doc for Notification Services", "0.1", (String) null,
				new Contact("AbhishekSharma", (String) null, "ja-abhisheksharma@mobiloitte.com"), (String) null,
				(String) null, (Collection) Collections.emptyList());
	}
}