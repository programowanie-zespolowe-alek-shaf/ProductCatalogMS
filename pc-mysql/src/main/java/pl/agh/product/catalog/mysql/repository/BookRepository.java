package pl.agh.product.catalog.mysql.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.agh.product.catalog.mysql.entity.Book;

import java.util.List;
import java.util.Set;

@Transactional
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(concat('%', ?1, '%')) OR b.author LIKE LOWER(concat('%', ?1, '%'))")
    Set<Book> findAllByTitleOrAuthorIsLikeIgnoreCase(String phrase);

    List<Book> findAll();
}

