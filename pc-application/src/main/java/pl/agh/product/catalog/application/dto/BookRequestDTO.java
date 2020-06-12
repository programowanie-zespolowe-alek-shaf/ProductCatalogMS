package pl.agh.product.catalog.application.dto;

import lombok.Data;
import pl.agh.product.catalog.mysql.entity.Book;
import pl.agh.product.catalog.mysql.entity.Category;

@Data
public class BookRequestDTO {
    private String title;
    private String author;
    private Integer categoryId;
    private Integer year;
    private String photoUrl;
    private String description;
    private Boolean available;
    private Float price;

    public Book toEntity(Category category) {
        return Book.builder()
                .title(title)
                .author(author)
                .category(category)
                .year(year)
                .photoUrl(photoUrl)
                .description(description)
                .available(available)
                .price(price)
                .build();
    }

    public Float getPrice() {
        return price != null ? (float) (Math.round(price * 100.0) / 100.0) : null;
    }
}
