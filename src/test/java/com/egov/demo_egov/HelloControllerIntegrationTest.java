package com.egov.demo_egov;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void helloEndpoint_shouldReturnHelloWorld() {
        // Given
        String url = "http://localhost:" + port + "/hello";

        // When
        String response = restTemplate.getForObject(url, String.class);

        // Then
        assertThat(response).isEqualTo("Hello World!");
    }

    @Test
    void actuatorHealthEndpoint_shouldReturnUp() {
        // Given
        String url = "http://localhost:" + port + "/actuator/health";

        // When
        String response = restTemplate.getForObject(url, String.class);

        // Then
        assertThat(response).contains("UP");
    }

    @Test
    void actuatorInfoEndpoint_shouldBeAccessible() {
        // Given
        String url = "http://localhost:" + port + "/actuator/info";

        // When & Then - Just verify it's accessible, content may be empty
        assertThat(restTemplate.getForEntity(url, String.class).getStatusCode().is2xxSuccessful()).isTrue();
    }
}