package com.tanvir.tenantmanagement.routingfilter.resolver;

import com.tanvir.core.config.AppConfig;
import com.tanvir.core.util.exception.ExceptionHandlerUtil;
import com.tanvir.tenantmanagement.util.TenantIdEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Slf4j
public class TenantResolver {

    private static boolean defaultSchemaEnabled;

    private static String defaultTenantName;

    public static Mono<String> resolve() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig config = ctx.getBean(AppConfig.class);
        defaultSchemaEnabled = config.getDefaultSchemaEnabled();
        defaultTenantName = config.getDefaultTenantName();
        return Mono.deferContextual(
            contextView -> {
                Optional<Object> optionalTenantId = contextView.getOrEmpty(TenantIdEnum.TENANT_ID_CONTEXT_KEY.getValue());
                if (optionalTenantId.isPresent()) {
                    return Mono.just(String.valueOf(optionalTenantId.get()))
                        .doOnNext(string -> log.info(" TENANT_ID_CONTEXT_KEY : {} value {}", TenantIdEnum.TENANT_ID_CONTEXT_KEY.getValue(), string));
                } else if (defaultSchemaEnabled) {
                    log.info("Default schema enabled, using default schema: {}", defaultTenantName);
                    return Mono.just(defaultTenantName);
                } else {
                    log.error("Tenant id not found in request !");
                    return Mono.error(new ExceptionHandlerUtil(HttpStatus.BAD_REQUEST, "Tenant id not found in request !"));
                }
            });
    }
}
