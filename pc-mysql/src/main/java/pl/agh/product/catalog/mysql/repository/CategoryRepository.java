package pl.agh.product.catalog.mysql.repository;

import org.springframework.data.repository.CrudRepository;
import pl.agh.product.catalog.mysql.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {

}

