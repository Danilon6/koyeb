package it.danilo.blog.buisnesslayer.dto;


public interface ArticleResponsePrj extends BaseProjection {

    Long getId();

    String getTitle();

    String getContent();

    User getAuthor();

    interface User {
        Long getId();
        String getFirstName();
        String getLastName();
    }


}
