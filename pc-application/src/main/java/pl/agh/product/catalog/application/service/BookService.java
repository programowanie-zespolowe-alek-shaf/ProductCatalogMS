package pl.agh.product.catalog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.agh.product.catalog.common.exception.BadRequestException;
import pl.agh.product.catalog.common.exception.CustomException;
import pl.agh.product.catalog.mysql.entity.Book;
import pl.agh.product.catalog.mysql.repository.BookRepository;
import pl.agh.product.catalog.mysql.repository.CategoryRepository;

import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public BookService(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    public Book find(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book add(Book book) throws CustomException {
        if (!categoryRepository.existsById(book.getCategory().getId())) {
            throw new BadRequestException("Category not found");
        }
        book.setId(null);
        book = bookRepository.save(book);
        return book;
    }

    public Book update(Long id, Book book) {
        if (!bookRepository.existsById(id)) {
            return null;
        }
        book.setId(id);
        book = bookRepository.save(book);
        return book;
    }

    public Book delete(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (!book.isPresent()) {
            return null;
        }
        bookRepository.delete(book.get());
        return book.get();
    }
}
