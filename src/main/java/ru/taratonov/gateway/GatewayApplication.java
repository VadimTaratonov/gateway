package ru.taratonov.gateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Api-gateway",
                description = "5 MVP level",
                version = "1.0.0",
                contact = @Contact(
                        name = "Taratonov Vadim",
                        email = "taratonovv8@bk.ru",
                        url = "https://github.com/VadimTaratonov/gateway"
                )
        )
)
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
