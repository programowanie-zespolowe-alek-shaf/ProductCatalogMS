package pl.agh.product.catalog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
            throw new BadRequestException("category not found");
        }
        Book book = bookRequestDTO.toEntity();
        book.setDateAdded(LocalDate.now());
        return bookRepository.save(book);
    }

    public Book update(Long id, BookRequestDTO bookRequestDTO) throws BadRequestException {
        Optional<Book> optBook = bookRepository.findById(id);
        if (!optBook.isPresent()) {
            return null;
        }
        if (!categoryRepository.existsById(bookRequestDTO.getCategory().getId())) {
            throw new BadRequestException("category not found");
        }
        Book existingBook = optBook.get();
        Book book = bookRequestDTO.toEntity();
        book.setId(id);
        book.setDateAdded(existingBook.getDateAdded());
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

    public Book updateBookPhotoUrl(Long id, String photoUrl) {
        Optional<Book> optBook = bookRepository.findById(id);
        if (!optBook.isPresent()) {
            return null;
        }
        Book book = optBook.get();
        book.setPhotoUrl(photoUrl);
        book = bookRepository.save(book);
        return book;
    }

    public ListResponse findBooks(int limit, int offset, Category category, Boolean recommended, String sort, String... phrases) {
        List<Book> books;
        if (phrases != null && phrases.length > 0) {
            books = findBooksByPhrases(phrases);
        } else {
            if (sort != null) {
                String[] sort_key_and_direction = sort.split(";");
                Sort sort_obj = null;
                if (sort_key_and_direction.length == 2 && sort_key_and_direction[1].equals("desc")) {
                    sort_obj = Sort.by(Sort.Direction.DESC, sort_key_and_direction[0]);
                } else {
                    sort_obj = Sort.by(Sort.Direction.ASC, sort_key_and_direction[0]);
                }
                books = (List<Book>)bookRepository.findAll(sort_obj);
            } else {
                books = bookRepository.findAll();
            }
        }

        if (category != null) {
            books = books.stream()
                    .filter(b -> category.getId().equals(b.getCategory().getId()))
                    .collect(Collectors.toList());
        }
        if (recommended != null) {
            books = books.stream()
                    .filter(b -> b.getRecommended().equals(recommended))
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
