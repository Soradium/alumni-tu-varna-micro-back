package org.acme.util.mappers;

import org.acme.avro.ambiguous.FacultyDto;
import org.acme.entites.Faculty;
import org.mapstruct.Mapper;

import jakarta.enterprise.context.ApplicationScoped;
@Mapper(componentModel = "cdi")
@ApplicationScoped
public abstract class FacultyMapper {
    public abstract FacultyDto toDto(Faculty entity);
    public abstract Faculty toEntity(FacultyDto dto);
}

