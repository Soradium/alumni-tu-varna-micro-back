package org.acme.util.mappers;

import org.acme.avro.ambiguous.SpecialityDto;
import org.acme.entites.Speciality;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public abstract class SpecialityMapper {
    public abstract SpecialityDto toDto(Speciality entity);

    public abstract Speciality toEntity(SpecialityDto dto);
}