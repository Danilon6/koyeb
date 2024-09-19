package it.danilo.blog.buisnesslayer.dto;

import it.danilo.blog.datalayer.entities.Roles;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RegisteredUserDTO {
    Long id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String firstName;
    String lastName;
    String email;
    private final List<Roles> roles;

    @Builder(setterPrefix = "with")
    public RegisteredUserDTO(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String firstName, String lastName, String username, String email, String profilePicture, String bio, List<Roles> roles, boolean enabled, boolean banned) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }
}
