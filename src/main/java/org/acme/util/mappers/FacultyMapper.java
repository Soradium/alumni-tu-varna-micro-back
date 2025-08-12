package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.avro.ambiguous.FacultyDto;
import org.acme.entites.Faculty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
@ApplicationScoped
public abstract class FacultyMapper {
    public abstract FacultyDto toDto(Faculty entity);

    public abstract Faculty toEntity(FacultyDto dto);
}

