package com.myhexin.webflux.repository;

import com.myhexin.webflux.model.MyEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

/**
 * @author zhangzhidong
 * @since 2019/1/7
 */
public interface MyEventRepository extends ReactiveMongoRepository<MyEvent, Long> {

    @Tailable
    Flux<MyEvent> findBy(); // 2
}
