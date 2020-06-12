package pl.agh.product.catalog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.agh.product.catalog.application.dto.BookRequestDTO;
import pl.agh.product.catalog.common.exception.BadRequestException;
import pl.agh.product.catalog.common.exception.CustomException;
import pl.agh.product.catalog.common.response.ListResponse;
import pl.agh.product.catalog.common.util.ListUtil;
import pl.agh.product.catalog.common.util.StringUtils;
import pl.agh.product.catalog.mysql.entity.Book;
import pl.agh.product.catalog.mysql.entity.Category;
import pl.agh.product.catalog.mysql.repository.BookRepository;
import pl.agh.product.catalog.mysql.repository.CategoryRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public Book find(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book add(BookRequestDTO bookRequestDTO) throws CustomException {
        var category = categoryRepository.findById(Long.valueOf(bookRequestDTO.getCategoryId()));
        if (category.isEmpty()) {
            throw new BadRequestException("category not found");
        }
        Book book = bookRequestDTO.toEntity(category.get());
        return bookRepository.save(book);
    }

    public Book update(Long id, BookRequestDTO bookRequestDTO) throws BadRequestException {
        if (!bookRepository.existsById(id)) {
            return null;
        }
        var category = categoryRepository.findById(Long.valueOf(bookRequestDTO.getCategoryId()));
        if (category.isEmpty()) {
            throw new BadRequestException("category not found");
        }
        Book book = bookRequestDTO.toEntity(category.get());
        book.setId(id);
        book = bookRepository.save(book);
        return book;
    }

    public Book delete(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            return null;
        }
        bookRepository.delete(book.get());
        return book.get();
    }

    public Book updateBookPhotoUrl(Long id, String photoUrl) {
        Optional<Book> optBook = bookRepository.findById(id);
        if (optBook.isEmpty()) {
            return null;
        }
        Book book = optBook.get();
        book.setPhotoUrl(photoUrl);
        book = bookRepository.save(book);
        return book;
    }

    public ListResponse findBooks(int limit, int offset, Category category, String... phrases) {
        List<Book> books;
        if (phrases != null && phrases.length > 0) {
            books = findBooksByPhrases(phrases);
        } else {
            books = bookRepository.findAll();
        }

        if (category != null) {
            books = books.stream()
                    .filter(b -> category.getId().equals(b.getCategory().getId()))
                    .collect(Collectors.toList());
        }
        int count = books.size();
        books = ListUtil.clampedSublist(books, limit, offset);
        return new ListResponse(books, count);
    }

    private List<Book> findBooksByPhrases(String... phrases) {
        for (int i = 0; i < phrases.length; i++) {
            phrases[i] = StringUtils.addSlashes(phrases[i]);
        }
        Set<Book> booksSet = new HashSet<>();
        for (String phrase : phrases) {
            if (!StringUtils.isBlank(phrase)) {
                booksSet.addAll(bookRepository.findAllByTitleOrAuthorIsLikeIgnoreCase(phrase));
            }
        }
        List<Book> books = new ArrayList<>(booksSet);
        Collections.sort(books);
        return books;
    }
}
