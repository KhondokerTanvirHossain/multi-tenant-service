package com.tanvir.tenantmanagement.master;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "tenants")
@Data
@Slf4j
public class TenantProperties {

    @Autowired
    MasterRepository masterRepository;

    public Map<String, String> getTenants() {
        return masterRepository.findAll()
                .stream()
                .collect(Collectors.toMap(MasterEntity::getOid, MasterEntity::getSchema));
    }
}
