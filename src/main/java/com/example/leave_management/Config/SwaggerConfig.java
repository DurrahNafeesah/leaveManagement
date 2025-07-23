package com.example.leave_management.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
        info=@Info(
                title = "Leave management APIs",
                description = "By durrah"
        ),
        servers=@Server(url = "http://localhost:8081",description = "local server")

)

public class SwaggerConfig {
        @Bean
        public OpenAPI customOpenAPI() {

                return new OpenAPI()
                        .info(new io.swagger.v3.oas.models.info.Info().title("JavaInUse Authentication Service"))
                        .addSecurityItem(new SecurityRequirement().addList("JavaInUseSecurityScheme"))
                        .components(new Components().addSecuritySchemes("JavaInUseSecurityScheme", new SecurityScheme()
                                .name("JavaInUseSecurityScheme").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));

        }

}