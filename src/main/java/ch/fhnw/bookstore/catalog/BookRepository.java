package ch.fhnw.bookstore.catalog;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public interface BookRepository extends JpaRepository<Book, String>, JpaSpecificationExecutor<Book> {

    /**
     * Sucht Bücher anhand von Schlüsselwörtern.
     * <ul>
     *   <li>Die Schlüsselwörter werden durch Leerzeichen getrennt.</li>
     *   <li>Jedes Schlüsselwort muss im Titel, in der Beschreibung ODER beim Autor vorkommen.</li>
     *   <li>Alle Schlüsselwörter müssen zutreffen (UND-Verknüpfung).</li>
     *   <li>Die Suche ist case-insensitive.</li>
     *   <li>Ist der Suchbegriff leer, werden alle Bücher zurückgegeben.</li>
     * </ul>
     */
    default List<Book> search(String query) {
        if (query == null || query.isBlank()) {
            return findAll();
        }
        Specification<Book> spec = Arrays.stream(query.trim().split("\\s+"))
                .map(BookRepository::containsKeyword)
                .reduce(Specification::and)
                .orElseThrow();
        return findAll(spec);
    }

    private static Specification<Book> containsKeyword(String keyword) {
        String pattern = "%" + keyword.toLowerCase(Locale.ROOT) + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), pattern),
                cb.like(cb.lower(root.get("description")), pattern),
                cb.like(cb.lower(root.get("author")), pattern)
        );
    }
}
