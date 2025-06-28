package com.sst.hash.info.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:8080/hash-info")
                .description("Local server");

        Server devServer = new Server()
                .url("https://dev.api.yourdomain.com")
                .description("Development server");

        Server prodServer = new Server()
                .url("https://api.yourdomain.com")
                .description("Production server");

        return new OpenAPI()
                .servers(Arrays.asList(localServer, devServer, prodServer));
    }

    // Optional: nếu bạn muốn chia group các APIs
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }
}
