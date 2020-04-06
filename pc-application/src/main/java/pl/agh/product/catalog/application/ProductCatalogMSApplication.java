package pl.agh.product.catalog.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(
        basePackages = {
                "pl.agh.product.catalog"
        }
)
@EntityScan("pl.agh.product.catalog")
@EnableJpaRepositories("pl.agh.product.catalog")
public class ProductCatalogMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCatalogMSApplication.class, args);
    }

}
