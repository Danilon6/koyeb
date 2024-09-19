package it.danilo.blog.buisnesslayer.services.interfaces;

import it.danilo.blog.buisnesslayer.dto.ArticleRequestDTO;
import it.danilo.blog.buisnesslayer.dto.ArticleResponseDTO;
import it.danilo.blog.buisnesslayer.dto.ArticleResponsePrj;

public interface ArticleService extends CRUDService<ArticleResponseDTO, ArticleRequestDTO, ArticleResponsePrj>{
}
