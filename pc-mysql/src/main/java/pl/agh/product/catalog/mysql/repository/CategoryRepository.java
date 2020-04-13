package pl.agh.product.catalog.mysql.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.agh.product.catalog.mysql.entity.Category;

import java.util.List;

@Transactional
@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);

    List<Category> findAll();
}

