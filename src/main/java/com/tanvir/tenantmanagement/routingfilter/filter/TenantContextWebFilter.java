package com.tanvir.tenantmanagement.routingfilter.filter;


import com.tanvir.core.config.AppConfig;
import com.tanvir.core.util.exception.ExceptionHandlerUtil;
import com.tanvir.tenantmanagement.util.TenantIdEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;



@Component
@Slf4j
public class TenantContextWebFilter implements WebFilter {

    private final String headerName;
    private final String defaultTenantName;
    private final boolean defaultSchemaEnabled;

    public TenantContextWebFilter() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig config = ctx.getBean(AppConfig.class);
        this.defaultSchemaEnabled = config.getDefaultSchemaEnabled();
        this.defaultTenantName = config.getDefaultTenantName();
        this.headerName = config.getHeaderName();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String headerValue = exchange.getRequest().getHeaders().getFirst(headerName);
        String contextKey = TenantIdEnum.TENANT_ID_CONTEXT_KEY.getValue();
        String contextValue;
        if (Optional.ofNullable(headerValue).isPresent()) {
            log.info("Tenant header name: {}, value: {}", headerName, headerValue);
            contextValue = Optional.of(headerValue).get();
        } else if (defaultSchemaEnabled) {
//            log.info("Default schema enabled, using default schema: {}", defaultTenantName);
            contextValue = defaultTenantName;
        } else {
            log.error("Tenant id not found in request !");
            return Mono.error(new ExceptionHandlerUtil(HttpStatus.BAD_REQUEST, "Tenant id not found in request !"));
        }
//        log.info("Setting context key: {}, value: {}", contextKey, contextValue);
        return chain.filter(exchange)
            .contextWrite(context -> context.put(contextKey, contextValue));
    }
}
