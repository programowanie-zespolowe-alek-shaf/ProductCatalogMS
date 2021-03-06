package pl.agh.product.catalog.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.agh.product.catalog.application.dto.BookRequestDTO;
import pl.agh.product.catalog.application.service.BookService;
import pl.agh.product.catalog.application.service.ValidationService;
import pl.agh.product.catalog.common.exception.CustomException;
import pl.agh.product.catalog.common.response.ListResponse;
import pl.agh.product.catalog.mysql.entity.Book;
import pl.agh.product.catalog.mysql.entity.Category;

import java.net.URI;

import static pl.agh.product.catalog.common.util.ResponseFormat.APPLICATION_JSON;

@RestController
@RequestMapping(value = BookController.PREFIX)
public class BookController {

    static final String PREFIX = "/books";

    private final BookService bookService;

    private final ValidationService validationService;

    @Autowired
    public BookController(BookService bookService, ValidationService validationService) {
        this.bookService = bookService;
        this.validationService = validationService;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.POST, produces = {APPLICATION_JSON})
    public ResponseEntity addBook(@RequestBody BookRequestDTO bookRequestDTO) throws CustomException {
        validationService.validate(bookRequestDTO);
        Book createdBook = bookService.add(bookRequestDTO);
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

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = {APPLICATION_JSON})
    public ResponseEntity updateBook(@PathVariable("id") Long id, @RequestBody BookRequestDTO bookRequestDTO) throws CustomException {
        validationService.validate(bookRequestDTO);
        Book updatedBook = bookService.update(id, bookRequestDTO);
        if (updatedBook == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedBook);
        }
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = {APPLICATION_JSON})
    public ResponseEntity deleteBook(@PathVariable Long id) {
        Book deletedBook = bookService.delete(id);
        if (deletedBook == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = {APPLICATION_JSON})
    public ResponseEntity findBooks(@RequestParam int limit,
                                    @RequestParam int offset,
                                    @RequestParam(required = false) Category category,
                                    @RequestParam(required = false) Boolean recommended,
                                    @RequestParam(required = false) String sort,
                                    @RequestParam(required = false) String... phrases) {
        ListResponse books = bookService.findBooks(limit, offset, category, recommended, sort, phrases);
        return ResponseEntity.ok(books);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "{id}", method = RequestMethod.PATCH, produces = {APPLICATION_JSON})
    public ResponseEntity updateBookImageUrl(@PathVariable("id") Long id, @RequestParam String photoUrl) {
        Book book = bookService.updateBookPhotoUrl(id, photoUrl);
        if (book == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(book);
        }
    }
}
