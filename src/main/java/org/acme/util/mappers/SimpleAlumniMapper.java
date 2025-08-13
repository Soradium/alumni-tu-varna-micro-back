package org.acme.util.mappers;

import org.acme.avro.back.AlumniDto;
import org.acme.entites.Alumni;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import jakarta.enterprise.context.ApplicationScoped;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
@ApplicationScoped
public abstract class SimpleAlumniMapper {

    @Mapping(target = "birthDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "degreeBuilder", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "facultyBuilder", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "linkedinUrl", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "memberships", ignore = true)
    public abstract AlumniDto toPartialDto(Alumni alumni);

}

