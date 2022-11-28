package com.ProductActive.ProductActive.service;

import com.ProductActive.ProductActive.entity.ProductActive;
import com.ProductActive.ProductActive.repository.ProductActiveRepository;
import com.ProductActive.ProductActive.web.mapper.ProductActiveMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductActiveService {

    @Autowired
    private ProductActiveRepository productActiveRepository;

    @Autowired
    private ProductActiveMapper productActiveMapper;

    public Flux<ProductActive> findAll(){
        log.debug("findAll executed");
        return productActiveRepository.findAll();
    }

    public Mono<ProductActive> findById(String productActiveId){
        log.debug("findById executed {}" , productActiveId);
        return productActiveRepository.findById(productActiveId);
    }

    public Mono<ProductActive> create(ProductActive productActive){
        log.debug("create executed {}",productActive);
        return productActiveRepository.save(productActive);
    }

    public Mono<ProductActive> update(String productActiveId, ProductActive productActive){
        log.debug("update executed {}:{}", productActiveId, productActive);
        return productActiveRepository.findById(productActiveId)
                .flatMap(dbProductActive -> {
                    productActiveMapper.update(dbProductActive, productActive);
                    return productActiveRepository.save(dbProductActive);
                });
    }

    public Mono<ProductActive>delete(String productActiveId){
        log.debug("delete executed {}",productActiveId);
        return productActiveRepository.findById(productActiveId)
                .flatMap(existingProductActive -> productActiveRepository.delete(existingProductActive)
                        .then(Mono.just(existingProductActive)));
    }




}
