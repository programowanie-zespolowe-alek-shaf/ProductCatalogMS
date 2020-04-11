package pl.agh.product.catalog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.agh.product.catalog.application.dto.BookRequestDTO;
import pl.agh.product.catalog.common.exception.BadRequestException;
import pl.agh.product.catalog.common.exception.CustomException;
import pl.agh.product.catalog.common.util.StringUtils;
import pl.agh.product.catalog.mysql.entity.Book;
import pl.agh.product.catalog.mysql.repository.BookRepository;
import pl.agh.product.catalog.mysql.repository.CategoryRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public Book add(BookRequestDTO bookRequestDTO) throws CustomException {
        if (!categoryRepository.existsById(bookRequestDTO.getCategory().getId())) {
            throw new BadRequestException("Category not found");
        }
        Book book = bookRequestDTO.toEntity();
        book = bookRepository.save(book);
        return book;
    }

    public Book update(Long id, BookRequestDTO bookRequestDTO) {
        if (!bookRepository.existsById(id)) {
            return null;
        }
        Book book = bookRequestDTO.toEntity();
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

    public Set<Book> findBooksByPhrase(String... phrases) {
        for (int i = 0; i < phrases.length; i++) {
            phrases[i] = StringUtils.addSlashes(phrases[i]);
        }
        Set<Book> books = new HashSet<>();
        for (String phrase : phrases) {
            if (!StringUtils.isBlank(phrase)) {
                books.addAll(bookRepository.findAllByTitleOrAuthorIsLikeIgnoreCase(phrase));
            }
        }
        return books;
    }

    public Book updateBookPhotoUrl(Long id, String photoUrl) {
        Optional<Book> optBook = bookRepository.findById(id);
        if(!optBook.isPresent()){
            return null;
        }
        Book book = optBook.get();
        book.setPhotoUrl(photoUrl);
        book = bookRepository.save(book);
        return book;
    }
}
