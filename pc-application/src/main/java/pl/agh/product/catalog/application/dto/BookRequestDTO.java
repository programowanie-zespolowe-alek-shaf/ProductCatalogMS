package pl.agh.product.catalog.application.dto;

import lombok.Data;
import pl.agh.product.catalog.mysql.entity.Book;
import pl.agh.product.catalog.mysql.entity.Category;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class BookRequestDTO {
    private String title;
    private String author;
    private Integer categoryId;
    private Integer year;
    private String photoUrl;
    private String description;
    private Boolean available;
    private Boolean recommended;
    private Float price;
    private Integer numPages;
    private Book.CoverType coverType = Book.CoverType.PAPERBACK;

    public Book toEntity(Category category) {
        return Book.builder()
                .title(title)
                .author(author)
                .category(category)
                .year(year)
                .photoUrl(photoUrl)
                .description(description)
                .available(available)
                .recommended(recommended)
                .price(price)
                .numPages(numPages)
                .coverType(coverType)
                .build();
    }

    public Float getPrice() {
        return price != null ? (float) (Math.round(price * 100.0) / 100.0) : null;
    }
}
