package com.zzd.resttemplatecaller.controller;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author zhangzhidong
 * @since 2019/1/7
 */
@RestController
public class HelloController {

    private final String TARGET_HOST = "http://localhost:8092";
    private RestTemplate restTemplate;

    public HelloController() {  // 1
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(1000);
        connectionManager.setMaxTotal(1000);
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().setConnectionManager(connectionManager).build()
        ));

    }

    @GetMapping("/hello/{latency}")
    public Mono<String> hello(@PathVariable int latency) {
        return Mono.fromCallable(() -> restTemplate.getForObject(TARGET_HOST + "/hello/" + latency, String.class))
                .subscribeOn(Schedulers.elastic());
    }
}
