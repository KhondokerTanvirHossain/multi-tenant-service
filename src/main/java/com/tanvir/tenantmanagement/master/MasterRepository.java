package com.tanvir.tenantmanagement.master;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MasterRepository {

    @Value("${multi-tenant-service.r2dbc.master.schema}")
    private String schema;

    @Value("${multi-tenant-service.r2dbc.master.tenant-info-table-name}")
    private String tableName;

    @Value("${multi-tenant-service.r2dbc.master.primary-key}")
    private String primaryKey;

    @Value("${multi-tenant-service.r2dbc.master.schema-name-column}")
    private String schemaColumnName;

    @Value("${multi-tenant-service.r2dbc.master.tenant-name-column}")
    private String tenantNameColumnName;

    private final MasterRepositoryConnection masterRepositoryConnection;

    public List<MasterEntity> findAll() {
        List<MasterEntity> instituteConfigEntities = new ArrayList<>();
        try (Connection con = masterRepositoryConnection.connectDB()) {
            String sql = "select * from " + schema + "." + tableName ;
            try (PreparedStatement p = con.prepareStatement(sql); ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    String oid = rs.getString(primaryKey);
                    String schemaName = rs.getString(schemaColumnName);
                    String tenantName = rs.getString(tenantNameColumnName);
                    log.info("Tenant Oid : {}, Name : {}, and Schema : {}", oid, tenantName, schemaName);
                    instituteConfigEntities.add(MasterEntity.builder().oid(oid).schema(schemaName).build());
                }
            }
        } catch (SQLException e) {
            log.error("Failed to retrieve tenant configurations:", e);
        } finally {
            masterRepositoryConnection.closeConnection();
        }
        return instituteConfigEntities;
    }


}

