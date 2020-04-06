package pl.agh.product.catalog.mysql.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "book", schema = "product")
public class Book {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //todo mm hidden for json request

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
    @ManyToOne(cascade = CascadeType.ALL)
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
}
