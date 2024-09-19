package it.danilo.blog.buisnesslayer.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class ArticleRequestDTO extends BaseDTO{
    private Long authorId;
    private String title;
    private String content;
    private MultipartFile picture;
}
