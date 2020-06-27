package com.about.cheese;

import com.about.cheese.api.CheeseController;
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
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(CheeseController.class)
class CheeseApplicationTestsUsingMock {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private CheeseRepository cheeseRepository;

//    @Autowired
//    private ApplicationContext context;
//
//    @BeforeEach
//    void beforeEach() {
//        webTestClient = WebTestClient
//                            .bindToApplicationContext(context)
//                            .configureClient()
//                            .baseUrl("/")
//                            .build();
//    }

    private Cheese createAndGetDummyCheese() {
        return Cheese.builder()
                .id("5ef79cb948c6791a4dda045d")
                .name("Cheddar")
                .origin("England")
                .typeOfMilk("Cow")
                .aging(5)
                .description("Cheddar cheese from England")
                .build();
    }

    @Test
    public void testGetCheeseId() {
        Cheese cheese = createAndGetDummyCheese();
        Mono<Cheese> cheeseMono = Mono.just(cheese);
        when(cheeseRepository.findById("5df755021bc3fc7b0231c8f9")).thenReturn(cheeseMono);
        webTestClient.get()
                .uri("/api/cheese/byId/5ef79cb948c6791a4dda045d")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cheese.class)
                .consumeWith(cheeseEntityExchangeResult -> {
                    Cheese response = cheeseEntityExchangeResult.getResponseBody();
                    assertEquals("Cheddar", response.getName());
                    assertEquals("England", response.getOrigin());
                    assertEquals("Cow", response.getTypeOfMilk());
                    assertEquals(5, response.getAging());
                    assertEquals("Cheddar cheese from England", response.getDescription());
                });
    }

    @Test
    public void testPostCheese() {
        Cheese cheese= createAndGetDummyCheese();
        Mono<Cheese> cheeseMono = Mono.just(cheese);
        when(cheeseRepository.save(cheese)).thenReturn(cheeseMono);
        webTestClient
            .post()
            .uri("/api/cheese")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(cheeseMono, Cheese.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody().isEmpty();
    }

    @Test
    public void testDeleteCheese() {
        Cheese cheese= createAndGetDummyCheese();
        Mono<Cheese> cheeseMono = Mono.just(cheese);
        when(cheeseRepository.findById("5ef79cb948c6791a4dda045e")).thenReturn(cheeseMono);
        when(cheeseRepository.delete(cheese)).thenReturn(Mono.empty());
        webTestClient
                .delete()
                .uri("/api/cheese/{id}", "5ef79cb948c6791a4dda045e")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }
}
