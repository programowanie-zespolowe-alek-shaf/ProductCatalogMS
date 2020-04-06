package pl.agh.product.catalog.mysql.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.agh.product.catalog.mysql.entity.Book;

import java.util.List;

@Transactional
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findAllByTitleIsLikeIgnoreCase(String phrase);

    List<Book> findAllByAuthorIsLikeIgnoreCase(String phrase);
}

