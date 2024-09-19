package it.danilo.blog.datalayer.repositories;

import it.danilo.blog.buisnesslayer.dto.ArticleResponsePrj;
import it.danilo.blog.datalayer.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<ArticleResponsePrj> findAllBy();
}
