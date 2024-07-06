package com.tanvir.tenantmanagement.config;

import com.tanvir.tenantmanagement.routingfilter.factory.TenantAwareConnectionFactory;
import com.tanvir.tenantmanagement.master.TenantProperties;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;
import java.util.HashMap;

import static io.r2dbc.pool.PoolingConnectionFactoryProvider.*;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories(
        basePackages = "com.tanvir.features",
        entityOperationsRef = "tenantEntityTemplate")
@EnableR2dbcAuditing
@RequiredArgsConstructor
@Slf4j
public class TenantAwareR2dbcConfig extends AbstractR2dbcConfiguration {

    private final HashMap<Object, Object> tenantConnectionFactoriesMap = new HashMap<>();

    @Value("${multi-tenant-service.r2dbc.host}")
    private String host;

    @Value("${multi-tenant-service.r2dbc.port}")
    private Integer port;

    @Value("${multi-tenant-service.r2dbc.username}")
    private String username;

    @Value("${multi-tenant-service.r2dbc.password}")
    private String password;

    @Value("${multi-tenant-service.r2dbc.database}")
    private String database;

    @Value("${spring.r2dbc.initial.size}")
    private Integer initialSize;

    @Value("${spring.r2dbc.max.size}")
    private Integer maxSize;

    @Value("${spring.r2dbc.max.idle.time}")
    private Integer maxIdleTime;

    private final TenantProperties tenantProperties;
    private final DefaultR2dbcConfig defaultR2DbcConfig;

    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Override
    @Bean("connectionFactory")
    @Qualifier("connectionFactory")
    public ConnectionFactory connectionFactory() {
        TenantAwareConnectionFactory tenantAwareConnectionFactory = new TenantAwareConnectionFactory();
        tenantAwareConnectionFactory.setDefaultTargetConnectionFactory(defaultR2DbcConfig.defaultConnectionFactory());
        tenantAwareConnectionFactory.setTargetConnectionFactories(tenantConnectionFactoriesMap);
        return tenantAwareConnectionFactory;
    }

    @Bean
    public R2dbcEntityOperations tenantEntityTemplate(
            @Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        return createEntityTemplate(connectionFactory);
    }

    private R2dbcEntityOperations createEntityTemplate(ConnectionFactory connectionFactory) {
        R2dbcDialect dialect = DialectResolver.getDialect(connectionFactory);
        DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(dialect);
        DatabaseClient databaseClient =
                DatabaseClient.builder()
                        .connectionFactory(connectionFactory)
                        .bindMarkers(dialect.getBindMarkersFactory())
                        .build();

        return new R2dbcEntityTemplate(databaseClient, strategy);
    }

    @PostConstruct
    public void initializeTenantDataSources() {
        tenantProperties.getTenants().forEach(this::createTenantConnectionFactory);
    }

    public void createTenantConnectionFactory(String tenantId, String schema) {
        ConnectionFactory tenantConnectionFactory =
                ConnectionFactories.get(
                        ConnectionFactoryOptions.builder()
                                .option(PROTOCOL, "postgresql")
                        .option(DRIVER, POOLING_DRIVER)
                        .option(HOST, host)
                        .option(PORT, port)
                        .option(USER, username)
                        .option(PASSWORD, password)
                        .option(DATABASE, database)
                        .option(Option.valueOf("schema"), schema)
                        .option(INITIAL_SIZE, initialSize)
                        .option(MAX_SIZE, maxSize)
                        .option(MAX_IDLE_TIME, Duration.ofMillis(maxIdleTime))
                                .build());
        tenantConnectionFactoriesMap.putIfAbsent(tenantId, tenantConnectionFactory);
        log.info("tenantConnectionFactoriesMap : {}", tenantConnectionFactoriesMap);
    }
}
