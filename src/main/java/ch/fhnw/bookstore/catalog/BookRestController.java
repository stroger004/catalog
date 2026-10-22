package ch.fhnw.bookstore.catalog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookRestController {

    private final BookRepository bookRepository;

    public BookRestController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Beispiel: GET /books?keywords=tolkien ring
     */
    @GetMapping
    public List<Book> searchBooks(@RequestParam(name = "keywords", required = false, defaultValue = "") String keywords) {
        return bookRepository.search(keywords);
    }
}
