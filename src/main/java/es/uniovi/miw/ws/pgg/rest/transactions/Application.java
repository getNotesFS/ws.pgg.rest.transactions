package es.uniovi.miw.ws.pgg.rest.transactions;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition(
        info = @Info(
                title = "WS PGG Transactions",
                version = "1.0",
                description = "API RESTful Web Service with Spring Boot",
                contact = @Contact(
                        name = "SM",
                        email = "smtester@uniovi.es"
                )
        ),
        servers = {
                @Server(
                        url = "${server.base-url}",
                        description = "Local server"
                )
        }
)
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
