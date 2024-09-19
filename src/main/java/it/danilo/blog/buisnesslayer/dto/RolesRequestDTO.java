package it.danilo.blog.buisnesslayer.dto;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class RolesRequestDTO extends BaseDTO{
    String roleType;
}
