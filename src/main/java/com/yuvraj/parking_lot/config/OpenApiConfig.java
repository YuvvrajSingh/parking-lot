package com.yuvraj.parking_lot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI parkingLotOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Parking Lot Management System API")
                        .description("REST API for managing parking lots, floors, spots, and vehicle parking")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Yuvraj")
                                .email("yuvraj@example.com")));
    }
}
