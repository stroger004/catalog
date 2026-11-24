package ch.fhnw.bookstore.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integrationstest: startet die ganze Anwendung auf einem zufälligen Port
 * und ruft die REST-Schnittstelle über echtes HTTP auf.
 * Verwendet eine PostgreSQL-Datenbank (Testcontainers) mit den Daten aus data.sql.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class CatalogApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void searchesSeedDataOverHttp() {
        ResponseEntity<Book[]> response = restTemplate.getForEntity(
                "/api/books?keywords={keywords}", Book[].class, "DÜRRENMATT physiker");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getTitle()).isEqualTo("Die Physiker");
    }

    @Test
    void returnsAllSeedBooksWithoutKeywords() {
        ResponseEntity<Book[]> response = restTemplate.getForEntity("/api/books", Book[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(10);
    }

    @Test
    void returnsEmptyArrayWhenNothingFound() {
        ResponseEntity<Book[]> response = restTemplate.getForEntity(
                "/api/books?keywords={keywords}", Book[].class, "Kochbuch");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }
}
