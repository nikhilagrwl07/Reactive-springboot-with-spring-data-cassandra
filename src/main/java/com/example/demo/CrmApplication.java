package com.example.demo;

import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class CrmApplication {

    private final String nikhil = "Nikhil", ritu = "Ritu", minakshi = "Minakshi";

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> ready(CustomerOrderRepository repository) {
        return event -> {
            Mono<Void> delete = repository.deleteAll();

            Flux<CustomerOrders> write = Flux.just(this.nikhil, this.ritu, this.minakshi)
                    .flatMap(name -> addOrdersFor(repository, name));

            Flux<CustomerOrders> allCustomerOrders = repository.findAll();

            delete
                    .thenMany(write)
                    .thenMany(allCustomerOrders.doOnNext(customerOrders -> System.out.println("repository : " + customerOrders.toString())))
                    .subscribe();
        };
    }

    private Flux<CustomerOrders> addOrdersFor(CustomerOrderRepository repository, String name) {
        UUID customerId = UUID.randomUUID();
        List<CustomerOrders> customerOrders = new ArrayList<CustomerOrders>();
        for (int i = 0; i < (Math.random() * 100); i++) {
            customerOrders.add(new CustomerOrders(customerId, UUID.randomUUID(), name));
        }
        return repository.saveAll(customerOrders);
    }

}
