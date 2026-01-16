package com.github.stueberm1.riskmanager.http.service;

import com.github.stueberm1.riskmanager.http.patch.JsonPatchOperationProcessingRiskPatchFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RiskManagerHttpConfiguration implements WebMvcConfigurer {



    @Bean
    public RiskModelConverter riskModelConverter() {
        return new RiskTORiskModelConverter();
    }

    @Bean
    public RiskPatchFactory riskJsonPatchFactory() {
        return new JsonPatchOperationProcessingRiskPatchFactory();
    }
}
