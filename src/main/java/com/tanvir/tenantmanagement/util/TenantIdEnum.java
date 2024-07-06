package com.tanvir.tenantmanagement.util;

import lombok.Getter;

@Getter
public enum TenantIdEnum {

    TENANT_ID_CONTEXT_KEY("context_tenant_id")
    ;
    private final String value;

    TenantIdEnum(String value) {
        this.value = value;
    }

}
