package pl.agh.product.catalog.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.agh.product.catalog.application.service.BookService;
import pl.agh.product.catalog.common.exception.CustomException;
import pl.agh.product.catalog.mysql.entity.Book;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static pl.agh.product.catalog.common.util.ResponseFormat.APPLICATION_JSON;

@RestController
@RequestMapping(value = BookController.PREFIX)
public class BookController {

    static final String PREFIX = "/books";

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = {APPLICATION_JSON})
    public ResponseEntity addBook(@RequestBody Book book) throws CustomException {
        Book createdBook = bookService.add(book);
        if (createdBook == null) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdBook.getId())
                    .toUri();

            return ResponseEntity.created(uri)
                    .body(createdBook);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = {APPLICATION_JSON})
    public ResponseEntity<Book> getBook(@PathVariable("id") Long id) {
        Book book = bookService.find(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(book);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = {APPLICATION_JSON})
    public ResponseEntity updateBook(@PathVariable("id") Long id, @RequestBody Book book) {
        Book updatedBook = bookService.update(id, book);
        if (updatedBook == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedBook);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = {APPLICATION_JSON})
    public ResponseEntity deleteBook(@PathVariable Long id) {
        Book deletedBook = bookService.delete(id);
        if (deletedBook == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @RequestMapping(value = "/findByPhrase", method = RequestMethod.GET, produces = {APPLICATION_JSON})
    public ResponseEntity findBookByPhrase(HttpServletRequest request) {
        return null;
    }

    @RequestMapping(value = "{id}/uploadImage", method = RequestMethod.PUT, produces = {APPLICATION_JSON})
    public ResponseEntity updateBookImageUrl(@PathVariable("id") Long id, HttpServletRequest request) {
        return null;
    }
}
