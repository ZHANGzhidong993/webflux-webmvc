package com.zzd.webflux.repository;

import com.zzd.webflux.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * @author zhangzhidong
 * @since 2019/1/5
 */
public interface UserRepository extends ReactiveCrudRepository<User, String> {

    Mono<User> findByUsername(String username);

    Mono<Long> deleteByUsername(String username);
}
