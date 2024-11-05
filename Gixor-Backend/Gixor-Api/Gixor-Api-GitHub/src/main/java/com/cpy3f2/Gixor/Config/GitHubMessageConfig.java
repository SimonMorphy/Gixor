package com.cpy3f2.Gixor.Config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class GitHubMessageConfig {
    @Bean
    public MessageSource githubMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("github-error-messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
} 