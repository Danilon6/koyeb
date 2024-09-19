package it.danilo.blog.buisnesslayer.services.interfaces;

import it.danilo.blog.buisnesslayer.dto.RolesRequestDTO;
import it.danilo.blog.buisnesslayer.dto.RolesResponseDTO;
import it.danilo.blog.buisnesslayer.dto.RolesResponsePrj;

public interface RolesService {

    RolesResponsePrj getAll();

    RolesResponseDTO save(RolesRequestDTO e);

    RolesResponseDTO delete(Long id);
}
