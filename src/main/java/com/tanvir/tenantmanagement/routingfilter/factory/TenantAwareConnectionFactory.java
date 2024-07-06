package com.tanvir.tenantmanagement.routingfilter.factory;

import com.tanvir.tenantmanagement.routingfilter.resolver.TenantResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Mono;

@Slf4j
public class TenantAwareConnectionFactory extends AbstractRoutingConnectionFactory {

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return TenantResolver.resolve().map(string -> string)
            .doOnNext(tenant -> log.info("Requested Tenant: {}", tenant))
            .map(Object::toString);
    }
}
