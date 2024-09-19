package it.danilo.blog.buisnesslayer.services.interfaces;

import it.danilo.blog.buisnesslayer.dto.BaseDTO;
import it.danilo.blog.buisnesslayer.dto.BaseProjection;


import java.io.IOException;
import java.util.List;

public interface CRUDService<T extends BaseDTO, A extends BaseDTO, B extends BaseProjection> {
    List<B> getAll();

    T getById(Long id);

    T save(A e) throws IOException;

    T update(Long id, A e) throws IOException;

    T delete(Long id);
}
