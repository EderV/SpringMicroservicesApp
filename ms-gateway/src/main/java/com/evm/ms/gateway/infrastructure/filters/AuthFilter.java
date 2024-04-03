package com.evm.ms.gateway.infrastructure.filters;

import com.evm.ms.gateway.domain.in.MsAuthServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {

    private final MsAuthServicePort msAuthService;

    public AuthFilter(@Lazy MsAuthServicePort msAuthService) {
        super(AbstractGatewayFilterFactory.NameConfig.class);

        this.msAuthService = msAuthService;
    }

    @Override
    public GatewayFilter apply(final AbstractGatewayFilterFactory.NameConfig config) {
        return ((exchange, chain) -> {
            String path = exchange.getRequest().getPath().value();
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            log.info("Path: " + path);
            log.info("Auth header: " + authHeader);

            int res = msAuthService.isClientAllowed(authHeader, path);
            if (res != 200) {
                var response = exchange.getResponse();

                switch (res) {
                    case 401 -> response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    case 403 -> response.setStatusCode(HttpStatus.FORBIDDEN);
                    default -> response.setStatusCode(HttpStatus.BAD_GATEWAY);
                }

                return response.setComplete();
            }

            return chain.filter(exchange);
        });
    }

}
