package com.company.watermark;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.company.watermark.domain"})
@EnableJpaRepositories(basePackages = {"com.company.watermark.repository"})
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class RepositoryConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> "any"; //we don't need information about who created/modified JPA entity
    }
}
