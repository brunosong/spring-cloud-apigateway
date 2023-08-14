package com.study.api_gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthController {

    private final Environment env;

    /* 스프링 클라우드 게이트 웨이도 컨트롤러가 사용 가능하다. */
    @GetMapping("/health_check")
    public String status() {
       return String.format("It's Working in User Service"
                + ", (local.server.port) is : " + env.getProperty("local.server.port")
                + ", (server.port) is : " + env.getProperty("server.port")
                + ", Token secret is : " + env.getProperty("token.secret")
                + ", Token expiration time is : " + env.getProperty("token.expiration_time"));

    }

}
