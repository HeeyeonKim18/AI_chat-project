package beside2.ten039;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@EnableJpaAuditing
@SpringBootApplication
public class Ten039Application {

    public static void main(String[] args) {
        SpringApplication.run(Ten039Application.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        String[] methods = {HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.OPTIONS.name(), HttpMethod.HEAD.name()};
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/oauth/**")
                        .allowedMethods(methods)
                        .allowCredentials(true)
                        .allowedOriginPatterns("http://localhost:3000", "https://bao-look.vercel.app");
                registry.addMapping("/chat/**")
                        .allowedMethods(methods)
                        .allowCredentials(true)
                        .allowedOrigins("http://localhost:3000", "https://bao-look.vercel.app");
                registry.addMapping("/calendar/**")
                        .allowedMethods(methods)
                        .allowCredentials(true)
                        .allowedOrigins("http://localhost:3000", "https://bao-look.vercel.app");
                registry.addMapping("/paper/**")
                        .allowedMethods(methods)
                        .allowCredentials(true)
                        .allowedOrigins("http://localhost:3000", "https://bao-look.vercel.app");
                registry.addMapping("/member/**")
                        .allowedMethods(methods)
                        .allowCredentials(true)
                        .allowedOrigins("http://localhost:3000", "https://bao-look.vercel.app");
            }
        };

    }
}
