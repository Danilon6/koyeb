package it.danilo.blog.buisnesslayer.services.interfaces;

public interface Mapper<S,D> {
    D map(S input);
}

