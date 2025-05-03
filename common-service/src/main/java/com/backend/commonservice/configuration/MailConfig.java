package com.backend.commonservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class MailConfig {
    @Primary
    @Bean
    public FreeMarkerConfigurationFactoryBean factoryBean(){
        FreeMarkerConfigurationFactoryBean factoryBean = new FreeMarkerConfigurationFactoryBean();
        factoryBean.setTemplateLoaderPath("classpath:templates/");
        factoryBean.setDefaultEncoding("UTF-8");
        return factoryBean;
    }
}
