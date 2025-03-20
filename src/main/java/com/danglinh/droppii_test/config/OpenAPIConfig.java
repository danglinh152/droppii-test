package com.danglinh.droppii_test.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("DROPPII - TEST API")
                        .description("THIS IS AN ASSIGNMENT FROM DROPPII")
                        .version("1.0").contact(new Contact().name("Dang Quang Khanh Linh")
                                .email("danglinh.k4@gmail.com").url("https://www.facebook.com/danglinh1502"))
                        .license(new License().name("License of API")
                                .url("API license URL")));
    }
}
