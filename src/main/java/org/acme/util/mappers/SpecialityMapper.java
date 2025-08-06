package org.acme.util.mappers;

import org.acme.avro.ambiguous.SpecialityDto;
import org.acme.entites.Speciality;
import org.mapstruct.Mapper;

import jakarta.enterprise.context.ApplicationScoped;

@Mapper(componentModel = "cdi")
@ApplicationScoped
public abstract class SpecialityMapper {
    public abstract SpecialityDto toDto(Speciality entity);
    public abstract Speciality toEntity(SpecialityDto dto);
}