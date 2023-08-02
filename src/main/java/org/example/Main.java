package org.example;

import org.example.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class Main implements CommandLineRunner {
    private static final WebClient webClient = WebClient.create("http://localhost:8181");

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        getAllUsers();
    }

    private static void getAllUsers() {
        webClient.get()
                .uri("/api/users")
                .retrieve()
                .bodyToFlux(User.class)
                .doOnNext(user -> System.out.println("User from Flux: " + user.getId()))
                .takeLast(1)
                .subscribe(user -> getUserById(user.getId()));
    }

    private static void getUserById(String id) {
        webClient.get()
                .uri("/api/users" + id)
                .retrieve()
                .bodyToMono(User.class)
                .subscribe(user -> System.out.println("User from Mono: " + user));
    }
}