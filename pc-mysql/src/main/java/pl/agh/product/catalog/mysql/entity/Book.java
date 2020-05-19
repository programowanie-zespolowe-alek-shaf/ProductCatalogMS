package pl.agh.product.catalog.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "book", schema = "product")
public class Book implements Comparable<Book> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "author")
    private String author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "year")
    private Integer year;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "available")
    private Boolean available;

    @NotNull
    @Column(name = "recommended")
    private Boolean recommended;

    @Column(name = "price")
    private Float price;

    @Column(name = "num_pages")
    private Integer numPages;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cover_type")
    private CoverType coverType;

    @NotNull
    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Override
    public int compareTo(Book o) {
        return this.id.compareTo(o.getId());
    }

    public enum CoverType {
        PAPERBACK, HARDCOVER
    }
}
