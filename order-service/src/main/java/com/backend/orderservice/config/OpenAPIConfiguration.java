/*
 * @(#) $(NAME).java    1.0     2/13/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.orderservice.config;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 21/2/2025 2:10 PM
 */

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:9292");
        server.setDescription("Order Management REST API Documentation");

        Info information = new Info()
                .title("Order Management REST API Documentation")
                .version("1.0")
                .description("This API exposes endpoints to manage orders.")
                .contact(
                        new Contact()
                                .email("phamkhuong436@gmail.com")
                                .name("Pham Kim Khuong")
                                .url("")
                );


        return new OpenAPI().info(information).servers(List.of(server));
    }

}
