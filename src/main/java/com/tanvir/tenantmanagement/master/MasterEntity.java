package com.tanvir.tenantmanagement.master;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterEntity implements Serializable {
    private String oid;
    private String name;
    private String schema;
}
