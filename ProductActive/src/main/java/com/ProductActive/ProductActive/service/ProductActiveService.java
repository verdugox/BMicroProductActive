package com.ProductActive.ProductActive.service;

import com.ProductActive.ProductActive.entity.Client;
import com.ProductActive.ProductActive.entity.ProductActive;
import com.ProductActive.ProductActive.repository.ProductActiveRepository;
import com.ProductActive.ProductActive.web.mapper.ProductActiveMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductActiveService {

    @Autowired
    private ProductActiveRepository productActiveRepository;

    @Autowired
    private ProductActiveMapper productActiveMapper;


    private final String BASE_URL = "http://localhost:9040";

    TcpClient tcpClient = TcpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
            .doOnConnected(connection ->
                    connection.addHandlerLast(new ReadTimeoutHandler(3))
                            .addHandlerLast(new WriteTimeoutHandler(3)));
    private final WebClient client = WebClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))  // timeout
            .build();

    public Mono<Client> findClientByDNI(String identityDni){
        return this.client.get().uri("/v1/client/findByIdentityDni/{identityDni}",identityDni)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Client.class);
    }



    public Flux<ProductActive> findAll(){
        log.debug("findAll executed");
        return productActiveRepository.findAll();
    }

    public Mono<ProductActive> findById(String productActiveId){
        log.debug("findById executed {}" , productActiveId);
        return productActiveRepository.findById(productActiveId);
    }

    public Mono<ProductActive> findByTypeCreditAndDocument(String typeCredit, String document){
        log.debug("findByTypeCreditAndDocument executed {}" , typeCredit, document);
        return productActiveRepository.findByTypeCreditAndDocument(typeCredit, document);
    }


    public Mono<ProductActive> create(ProductActive productActive){
        log.debug("create executed {}", productActive);
        Mono<Client> client = findClientByDNI(productActive.getDocument());
        return client.switchIfEmpty(Mono.error(new Exception("Client Not Found" + productActive.getDocument())))
                .flatMap(client1 -> {
                    if(client1.getTypeClient().equals("EMPRESARIAL")){
                        return findByTypeCreditAndDocument(productActive.getTypeCredit(),productActive.getDocument())
                                .flatMap(account1 -> {
                                    if(account1.getTypeCredit().equals("PERSONAL")){
                                        return Mono.error(new Exception("No puede realizar más de un registro Personal."));
                                    }
                                    else{
                                        return productActiveRepository.save(productActive);
                                    }
                                }).switchIfEmpty(productActiveRepository.save(productActive));
                    }
                    else
                    {
                            return Mono.error(new Exception("El Producto Activo que desea registrar, tiene asociado un DNI de tipo cliente personal" + client1.getTypeClient()));
                    }
                });
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
