package pl.agh.product.catalog.mysql.repository;

import org.springframework.data.repository.CrudRepository;
import pl.agh.product.catalog.mysql.entity.Book;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findAllByTitleIsLike(String phase);

    List<Book> findAllByAuthorIsLike(String phase);
}

