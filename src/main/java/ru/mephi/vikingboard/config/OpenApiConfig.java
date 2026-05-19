package ru.mephi.vikingboard.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vikingBoardOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Viking Board API")
                .version("1.0.0")
                .description("REST API для добавления, удаления и полной перезаписи записей о викингах"));
    }
}
