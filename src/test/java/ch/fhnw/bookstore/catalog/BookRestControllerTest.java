package ch.fhnw.bookstore.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookRepository repository;

    @Test
    void returnsSearchResultAsJson() throws Exception {
        when(repository.search("tolkien")).thenReturn(List.of(
                new Book("1", "Der Hobbit", "Bilbo und die Zwerge.", "J.R.R. Tolkien")));

        mockMvc.perform(get("/books").param("keywords", "tolkien"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isbn").value("1"))
                .andExpect(jsonPath("$[0].title").value("Der Hobbit"))
                .andExpect(jsonPath("$[0].description").value("Bilbo und die Zwerge."))
                .andExpect(jsonPath("$[0].author").value("J.R.R. Tolkien"));

        verify(repository).search("tolkien");
    }

    @Test
    void returnsEmptyJsonArrayWhenNothingFound() throws Exception {
        when(repository.search("xyz")).thenReturn(List.of());

        mockMvc.perform(get("/books").param("keywords", "xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void usesEmptyQueryWhenParamMissing() throws Exception {
        when(repository.search("")).thenReturn(List.of());

        mockMvc.perform(get("/books")).andExpect(status().isOk());

        verify(repository).search("");
    }
}
