package com.about.cheese;

import com.about.cheese.model.Cheese;
import com.about.cheese.repository.CheeseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CheeseApplicationTestsUsingServer {

    WebTestClient webTestClient;

    @Autowired
    private CheeseRepository cheeseRepository;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void beforeEach() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8085").build();
    }

    private Cheese createAndGetDummyCheese() {
        return Cheese.builder()
                .name("Cheddar")
                .origin("England")
                .typeOfMilk("Cow")
                .aging(5)
                .description("Cheddar cheese from England")
                .build();
    }

    @Test
    public void testGetCheeseId() {
        webTestClient.get()
                .uri("/api/cheese/byId/5df8edb19d7893327de70d42")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cheese.class)
                .consumeWith(cheeseEntityExchangeResult -> {
                    Cheese response = cheeseEntityExchangeResult.getResponseBody();
                    assertEquals("Roquefort", response.getName());
                    assertEquals("France", response.getOrigin());
                    assertEquals("Sheep", response.getTypeOfMilk());
                    assertEquals(5, response.getAging());
                });
    }

    @Test
    public void testPostCheese() {
        webTestClient
                .post()
                .uri("/api/cheese")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(createAndGetDummyCheese()), Cheese.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Cheese.class)
                .consumeWith(cheeseEntityExchangeResult -> {
                    Cheese response = cheeseEntityExchangeResult.getResponseBody();
                    assertNotNull(response.getId());
                });
    }

    @Test
    public void testDeleteCheese() {
        Mono<Cheese> cheese = cheeseRepository.findByName("Cheddar");
        cheese.subscribe(cheese1 -> System.out.println(cheese1.getId()));
        String id;
//        webTestClient
//                .delete()
//                .uri("/api/cheese/{id}", cheese.map(cheese1 -> cheese1.getId()))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody().isEmpty();
    }
}
