package com.about.cheese;

import com.about.cheese.model.Cheese;
import com.about.cheese.repository.CheeseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class CheeseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheeseApplication.class, args);
    }

}
