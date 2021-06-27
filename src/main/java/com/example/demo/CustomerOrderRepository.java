package com.example.demo;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;
import java.util.UUID;

public interface CustomerOrderRepository extends ReactiveCassandraRepository<CustomerOrders, CustomerOrdersPrimaryKey> {

    Flux<CustomerOrders> findByCustomerId(UUID uuid);
}
