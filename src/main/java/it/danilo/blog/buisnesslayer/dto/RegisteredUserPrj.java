package it.danilo.blog.buisnesslayer.dto;

import it.danilo.blog.datalayer.entities.Roles;

import java.time.LocalDateTime;
import java.util.List;

public interface RegisteredUserPrj extends BaseProjection {

    Long getId();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    String getFirstName();

    String getLastName();

    String getEmail();

    List<Roles> getRoles();
}
