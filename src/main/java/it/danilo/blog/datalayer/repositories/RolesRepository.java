package it.danilo.blog.datalayer.repositories;

import it.danilo.blog.buisnesslayer.dto.RolesResponsePrj;
import it.danilo.blog.datalayer.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {
   Optional<Roles> findOneByRoleType (String roleType);
   RolesResponsePrj findAllBy();
}
