package seoul.AutoEveryDay.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Swagger 설정 파일 입니다.
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/gasStation/history", "/center/drive/history"};

        return GroupedOpenApi.builder()
                .group("주행시험장 API")
                .pathsToMatch(paths)
                .build();
    }
    @Bean
    public OpenAPI apiDocConfig() {
        SecurityScheme auth = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.COOKIE).name("JSESSIONID");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("basicAuth");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("basicAuth", auth))
                .addSecurityItem(securityRequirement)
                .info(new Info()
                        .title("주행시험장 API")
                        .description("현대오토에버 주행시험장 API 입니다.")
                        .version("0.1.0")
                        .contact(new Contact()
                                .name("김민석")));
    }
}
