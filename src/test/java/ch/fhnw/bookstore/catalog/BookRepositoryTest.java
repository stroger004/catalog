package ch.fhnw.bookstore.catalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = "spring.sql.init.mode=never") // eigene Testdaten statt data.sql
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.saveAll(List.of(
                new Book("1", "Der Herr der Ringe", "Frodo zerstört den einen Ring.", "J.R.R. Tolkien"),
                new Book("2", "Der Hobbit", "Bilbo und die Zwerge.", "J.R.R. Tolkien"),
                new Book("3", "Clean Code", "Agile software craftsmanship.", "Robert C. Martin"),
                new Book("4", "Die Physiker", "Drei Physiker im Irrenhaus.", "Friedrich Dürrenmatt")
        ));
    }

    @Test
    void findsBookByTitle() {
        assertThat(isbns(repository.search("Hobbit"))).containsExactly("2");
    }

    @Test
    void findsBookByDescription() {
        assertThat(isbns(repository.search("Irrenhaus"))).containsExactly("4");
    }

    @Test
    void findsBooksByAuthor() {
        assertThat(isbns(repository.search("Tolkien"))).containsExactlyInAnyOrder("1", "2");
    }

    @Test
    void searchIsCaseInsensitive() {
        assertThat(isbns(repository.search("tOLKIEN"))).containsExactlyInAnyOrder("1", "2");
    }

    @Test
    void allKeywordsMustMatch() {
        // "Tolkien" passt auf 1 und 2, "Ring" nur auf 1
        assertThat(isbns(repository.search("Tolkien Ring"))).containsExactly("1");
    }

    @Test
    void keywordsMayMatchDifferentFields() {
        // "Hobbit" im Titel, "Zwerge" in der Beschreibung, "Tolkien" beim Autor
        assertThat(isbns(repository.search("hobbit zwerge tolkien"))).containsExactly("2");
    }

    @Test
    void findsPartialWords() {
        assertThat(isbns(repository.search("craft"))).containsExactly("3");
    }

    @Test
    void returnsEmptyListWhenOneKeywordDoesNotMatch() {
        assertThat(repository.search("Tolkien Dürrenmatt")).isEmpty();
    }

    @Test
    void returnsEmptyListForUnknownKeyword() {
        assertThat(repository.search("Kochbuch")).isEmpty();
    }

    @Test
    void returnsAllBooksForEmptyQuery() {
        assertThat(repository.search("")).hasSize(4);
        assertThat(repository.search("   ")).hasSize(4);
        assertThat(repository.search(null)).hasSize(4);
    }

    @Test
    void ignoresMultipleSpacesBetweenKeywords() {
        assertThat(isbns(repository.search("  Tolkien    Ring "))).containsExactly("1");
    }

    private static List<String> isbns(List<Book> books) {
        return books.stream().map(Book::getIsbn).toList();
    }
}
