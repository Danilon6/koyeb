package it.danilo.blog.buisnesslayer.dto;

import java.time.LocalDateTime;

public interface RolesResponsePrj extends BaseProjection{
    Long getId();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    String getRoleType();
}
