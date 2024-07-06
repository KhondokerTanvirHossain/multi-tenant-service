package com.tanvir.core.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:application.properties")
@Slf4j
public class AppConfig {

    @Value("${multi-tenant-service.r2dbc.default.schema.enabled}")
    private boolean defaultSchemaEnabled;

    @Value("${multi-tenant-service.tenant.default-tenant-name}")
    private String defaultTenantName;

    @Value("${multi-tenant-service.rest-api.tenant.header-name}")
    private String headerName;


    public boolean getDefaultSchemaEnabled() {
        return defaultSchemaEnabled;
    }
}
