package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.avro.ambiguous.DegreeDto;
import org.acme.entites.Degree;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
@ApplicationScoped
public abstract class DegreeMapper {
    public abstract DegreeDto toDto(Degree entity);

    @Mapping(source = "degreeName", target = "degree")
    public abstract Degree toEntity(DegreeDto dto);
}
