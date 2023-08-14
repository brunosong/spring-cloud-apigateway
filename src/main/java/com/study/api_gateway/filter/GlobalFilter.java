package com.study.api_gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    /*
        공통으로 사용할수 있는 필터 (만드는 법은 기본 필터랑 똑같음)
        호출되는 과정상 공통필터는 모든 필터 중에 가장 먼저 시작하고 가장 마지막에 종료가 된다.
    */

    public GlobalFilter() { super(Config.class); }

    @Override
    public GatewayFilter apply(Config config) {

        // Pre 필터
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global PRE filter : baseMessage : {}" , config.getBaseMessage() );

            if(config.isPreLogger()) {
                log.info("Global filter Start : request id -> {}" , request.getId() );
            }


            // POST 필터
            return chain.filter(exchange).then(
                    Mono.fromRunnable( () -> {

                        if(config.isPostLogger()) {
                            log.info("Global filter Exit : response code -> {}" , response.getStatusCode());
                        }

                    })
            );

        };


    }

    /* 필요한 컨피그를 설정 한다면 여기에 하면 된다.*/
    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

}
