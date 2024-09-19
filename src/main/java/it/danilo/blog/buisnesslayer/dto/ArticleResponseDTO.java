package it.danilo.blog.buisnesslayer.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class ArticleResponseDTO extends BaseDTO{
    Long id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    private UserResponsePartialDTO author;
    private String title;
    private String content;
    private String picture;
}
