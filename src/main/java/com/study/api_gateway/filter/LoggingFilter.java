package com.study.api_gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() { super(Config.class); }

    @Override
    public GatewayFilter apply(Config config) {

        GatewayFilter filter = null;

        filter = new OrderedGatewayFilter(new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                ServerHttpRequest request = exchange.getRequest();
                ServerHttpResponse response = exchange.getResponse();

                log.info("Logging filter : baseMessage : {}" , config.getBaseMessage() );

                if(config.isPreLogger()) {
                    log.info("Logging PRE filter : request id -> {}" , request.getId() );
                }

                Mono<Void> then = chain.filter(exchange)
                        .then(
                                Mono.fromRunnable(() -> {
                                    if (config.isPostLogger()) {
                                        log.info("Logging filter end : response code -> {}", response.getStatusCode());
                                    }
                                })
                        );

                return then;

            }
        }, Ordered.LOWEST_PRECEDENCE );

        return filter;

    }

    /* */
    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

}
