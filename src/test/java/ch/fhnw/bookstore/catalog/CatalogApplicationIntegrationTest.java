package ch.fhnw.bookstore.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-End-Test: echte H2-Datenbank mit den Daten aus data.sql.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CatalogApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void searchesSeedDataEndToEnd() throws Exception {
        mockMvc.perform(get("/books").param("keywords", "DÜRRENMATT physiker"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Die Physiker"));
    }

    @Test
    void returnsAllSeedBooksWithoutKeywords() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));
    }
}
