package com.about.cheese.repository;

import com.about.cheese.model.Cheese;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CheeseRepository extends ReactiveMongoRepository<Cheese, String> {
    Mono<Cheese> findByName(String name);
}
