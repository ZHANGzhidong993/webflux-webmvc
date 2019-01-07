package com.myhexin.webflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author zhangzhidong
 * @since 2019/1/2
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Welcome to reactive world ~");
    }

    @GetMapping("/hello/{latency}")
    public Mono<String> hello(@PathVariable int latency) {

        return Mono.just("Welcome to reactive world ~")
                .delayElement(Duration.ofMillis(latency));

    }
}
