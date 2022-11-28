package com.ProductActive.ProductActive.repository;

import com.ProductActive.ProductActive.entity.ProductActive;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProductActiveRepository extends ReactiveMongoRepository<ProductActive, String> {
    Mono<ProductActive> findByIdentityContract(String identityContract);
}
