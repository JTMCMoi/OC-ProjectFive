package com.openclassrooms.mddapi.services.mappers;


public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

}