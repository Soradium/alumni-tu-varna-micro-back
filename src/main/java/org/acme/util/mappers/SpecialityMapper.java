package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.avro.ambiguous.SpecialityDto;
import org.acme.entites.Speciality;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
@ApplicationScoped
public abstract class SpecialityMapper {
    public abstract SpecialityDto toDto(Speciality entity);

    public abstract Speciality toEntity(SpecialityDto dto);
}