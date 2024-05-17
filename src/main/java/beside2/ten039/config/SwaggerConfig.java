package beside2.ten039.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    private static final String SERVICE_NAME = "312TEN039";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "312TEN039 PROJECT";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }


    private Info apiInfo() {
        return new Info()
                .title(SERVICE_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION);
    }
}
