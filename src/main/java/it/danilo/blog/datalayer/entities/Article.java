package it.danilo.blog.datalayer.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "articles")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class Article extends BaseEntity{

    @ManyToOne
    private User author;

    @Column(length = 50, nullable = false, unique = true)
    private String title;

    @Column(length = 50, nullable = false)
    private String content;

    private String picture;

}
