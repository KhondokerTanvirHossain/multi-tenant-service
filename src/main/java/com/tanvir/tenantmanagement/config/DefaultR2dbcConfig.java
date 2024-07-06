package com.tanvir.tenantmanagement.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

import java.time.Duration;

import static io.r2dbc.pool.PoolingConnectionFactoryProvider.*;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableR2dbcRepositories(
        basePackages = "com.tanvir.common",
        entityOperationsRef = "defaultTenantEntityTemplate")
@RequiredArgsConstructor
@Slf4j
public class DefaultR2dbcConfig {

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

    @Value("${multi-tenant-service.r2dbc.default.schema}")
    private String defaultSchema;

    @Value("${spring.r2dbc.initial.size}")
    private Integer initialSize;

    @Value("${spring.r2dbc.max.size}")
    private Integer maxSize;

    @Value("${spring.r2dbc.max.idle.time}")
    private Integer maxIdleTime;



    @Bean
    public R2dbcEntityOperations defaultTenantEntityTemplate(
            @Qualifier("defaultConnectionFactory") ConnectionFactory connectionFactory) {
        return createEntityTemplate(connectionFactory);
    }

    @Bean
    @Qualifier("defaultConnectionFactory")
    public ConnectionFactory defaultConnectionFactory() {
        ConnectionFactory connectionFactory = ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(PROTOCOL, "postgresql")
                        .option(DRIVER, POOLING_DRIVER)
                        .option(HOST, host)
                        .option(PORT, port)
                        .option(USER, username)
                        .option(PASSWORD, password)
                        .option(DATABASE, database)
                        .option(Option.valueOf("schema"), defaultSchema)
                        .option(INITIAL_SIZE, initialSize)
                        .option(MAX_SIZE, maxSize)
                        .option(MAX_IDLE_TIME, Duration.ofMillis(maxIdleTime))
                        .build());
        return new ConnectionPool(ConnectionPoolConfiguration.builder(connectionFactory).build());
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
}
