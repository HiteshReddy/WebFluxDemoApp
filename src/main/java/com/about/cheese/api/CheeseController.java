package com.about.cheese.api;

import com.about.cheese.exception.CheeseNotFoundException;
import com.about.cheese.model.Cheese;
import com.about.cheese.repository.CheeseRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@RestController
@RequestMapping(value = "/api/cheese")
public class CheeseController {

    CheeseRepository cheeseRepository;

    public CheeseController(CheeseRepository cheeseRepository) {
        this.cheeseRepository = cheeseRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Cheese> getAllTypesOfCheese() {
        return cheeseRepository.findAll();
    }

    @GetMapping("/byId/{id}")

    public Mono<ResponseEntity<Cheese>> getCheeseById(@PathVariable(value = "id") String id) {
        Mono<Cheese> cheeseById = cheeseRepository.findById(id);
        return cheeseById
                .map(cheese -> {
                    return ResponseEntity.ok(cheese);
                })
                .onErrorMap(error -> new CheeseNotFoundException("No Cheese available for the given ID"))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/byName/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Cheese>> getCheeseByName(@PathVariable(value = "name") String name) {
        Supplier<ResponseEntity> supplier = (() -> {
            System.out.println(">>> nothing is returned from the database");
            return ResponseEntity.notFound().build();
        });
        return cheeseRepository.findByName(name)
                .map(cheese -> ResponseEntity.ok(cheese))
                .defaultIfEmpty(supplier.get());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cheese> createNewCheese(@RequestBody Cheese cheese) {
        return cheeseRepository.save(cheese);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Cheese>> updateCheese(@PathVariable(value = "id") String id, @RequestBody Cheese cheese) {
        return cheeseRepository.findById(id)
                .flatMap(existingCheese -> {
                    existingCheese.setName(cheese.getName());
                    existingCheese.setOrigin(cheese.getOrigin());
                    existingCheese.setTypeOfMilk(cheese.getTypeOfMilk());
                    existingCheese.setAging(cheese.getAging());
                    existingCheese.setDescription(cheese.getDescription());
                    return cheeseRepository.save(existingCheese);
                })
                .map(updatedCheese -> ResponseEntity.ok(updatedCheese))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCheeseById(@PathVariable(value = "id") String id) {
        return cheeseRepository.findById(id)
                .flatMap(existingProduct ->
                        cheeseRepository.delete(existingProduct)
                                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteAllCheese() {
        return cheeseRepository.deleteAll();
    }

}
