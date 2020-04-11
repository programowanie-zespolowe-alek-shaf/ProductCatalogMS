package pl.agh.product.catalog.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book", schema = "product")
public class Book implements Comparable<Book> {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Getter
    @Setter
    @Column(name = "title")
    private String title;

    @NotNull
    @Getter
    @Setter
    @Column(name = "author")
    private String author;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Getter
    @Setter
    @Column(name = "year")
    private Integer year;

    @Getter
    @Setter
    @Column(name = "photo_url")
    private String photoUrl;

    @Getter
    @Setter
    @Column(name = "description")
    private String description;

    @Getter
    @Setter
    @Column(name = "available")
    private Boolean available;

    @Getter
    @Setter
    @Column(name = "price")
    private Float price;

    @Override
    public int compareTo(Book o) {
        return this.id.compareTo(o.getId());
    }
}
