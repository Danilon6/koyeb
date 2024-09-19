package it.danilo.blog.buisnesslayer.services.impl;


import it.danilo.blog.buisnesslayer.dto.RolesRequestDTO;
import it.danilo.blog.buisnesslayer.dto.RolesResponseDTO;
import it.danilo.blog.buisnesslayer.dto.RolesResponsePrj;
import it.danilo.blog.buisnesslayer.services.interfaces.Mapper;
import it.danilo.blog.buisnesslayer.services.interfaces.RolesService;
import it.danilo.blog.datalayer.entities.Roles;
import it.danilo.blog.datalayer.repositories.RolesRepository;
import it.danilo.blog.presentationlayer.api.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {


    private final RolesRepository rolesRepository;

    private final Mapper<RolesRequestDTO, Roles> mapRolesRequestDTOToRoles;

    private final Mapper<Roles, RolesResponseDTO> mapRolesToRolesResponseDTO;

    @Override
    public RolesResponseDTO save(RolesRequestDTO role) {
            return
                    mapRolesToRolesResponseDTO.map(

                    mapRolesRequestDTOToRoles.map(role)
            );
    }

    @Override
    public RolesResponsePrj getAll() {
        return rolesRepository.findAllBy();
    }


    @Override
    public RolesResponseDTO delete(Long id) {
        var r = rolesRepository.findById(id).orElseThrow(()-> new NotFoundException(id));
        rolesRepository.delete(r);
        return mapRolesToRolesResponseDTO.map(r);
    }
}
