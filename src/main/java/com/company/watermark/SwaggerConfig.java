package com.company.watermark;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.not;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.watermark.title}")
    private String title;
    @Value("${swagger.watermark.description}")
    private String description;
    @Value("${swagger.watermark.version}")
    private String version;
    @Value("${swagger.watermark.contact.name}")
    private String contactName;
    @Value("${swagger.watermark.contact.url}")
    private String contactURL;
    @Value("${swagger.watermark.contact.email}")
    private String contactEmail;
    @Value("${swagger.watermark.license}")
    private String license;
    @Value("${swagger.watermark.licenseUrl}")
    private String licenseUrl;

    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(paths())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(licenseUrl)
                .contact(new Contact(contactName, contactURL, contactEmail))
                .license(license)
                .version(version)
                .build();
    }

    private Predicate<String> paths() {
        return not(regex("/error*"));
    }
}
