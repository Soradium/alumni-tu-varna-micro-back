package org.acme.util.mappers;

import org.acme.avro.ambiguous.DegreeDto;
import org.acme.entites.Degree;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public abstract class DegreeMapper {
    public abstract DegreeDto toDto(Degree entity);

    @Mapping(source = "degreeName", target = "degree")
    public abstract Degree toEntity(DegreeDto dto);
}
