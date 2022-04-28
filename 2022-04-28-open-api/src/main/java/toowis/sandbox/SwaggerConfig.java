package toowis.sandbox;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("toowis-admin")
                .pathsToMatch("/api/admin/**")
                .packagesToScan("toowis.sandbox.admin")
                .build();
    }
    
    @Bean
    public GroupedOpenApi onlineApi() {
        return GroupedOpenApi.builder()
                .group("toowis-online")
                .pathsToMatch("/api/online/**")
                .packagesToScan("toowis.sandbox.online")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Toowis Demo API")
                .description("SpringBoot Demo 웹 어플리케이션 API")
                .version("v0.0.1")
                .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0\"")))
                .externalDocs(new ExternalDocumentation()
                .description("SpringShop Wiki Documentation")
                .url("https://springshop.wiki.github.org/docs"));
    }
}
