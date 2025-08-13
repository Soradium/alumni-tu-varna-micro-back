package org.acme.util.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.acme.avro.ambiguous.AlumniGroupDtoSimplified;
import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.entites.AlumniGroup;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA, uses = {
        FacultyMapper.class, SpecialityMapper.class
})
@ApplicationScoped
public abstract class AlumniGroupMapper {

    private FacultyMapper facultyMapper;
    private SpecialityMapper specialityMapper;

    @Named("toAlumniGroupEntity")
    public AlumniGroup toEntity(AlumniGroupBackDto dto) {
        AlumniGroup entity = new AlumniGroup();

        entity.setId(dto.getId());
        entity.setGroupNumber(dto.getGroupNumber());
        entity.setGraduationYear(dto.getGraduationYear());
        entity.setFaculty(facultyMapper.toEntity(dto.getFaculty()));
        entity.setSpeciality(specialityMapper.toEntity(dto.getSpeciality()));
        
        return entity;
    }

    public List<AlumniGroupBackDto> toDtoList(List<AlumniGroup> list) {
        return list.stream().map(m -> {
            AlumniGroupBackDto dto = new AlumniGroupBackDto();

            dto.setId(dto.getId());
            dto.setGroupNumber(dto.getGroupNumber());
            dto.setGraduationYear(dto.getGraduationYear());
            dto.setFaculty(facultyMapper.toDto(m.getFaculty()));
            dto.setSpeciality(specialityMapper.toDto(m.getSpeciality()));

            return dto;

        }).collect(Collectors.toList());
    }


    @Named("toAlumniGroupEntitySimplified")
    public AlumniGroup toEntitySimplified(AlumniGroupDtoSimplified dto) {
        AlumniGroup entity = new AlumniGroup();

        entity.setId(dto.getId());
        entity.setGroupNumber(dto.getGroupNumber());
        entity.setGraduationYear(dto.getGraduationYear());
        entity.setFaculty(facultyMapper.toEntity(dto.getFaculty()));
        entity.setSpeciality(specialityMapper.toEntity(dto.getSpeciality()));
        entity.setMemberships(new ArrayList<>());

        return entity;
    }

    @Inject
    public void setFacultyMapper(FacultyMapper facultyMapper) {
        this.facultyMapper = facultyMapper;
    }

    @Inject
    public void setSpecialityMapper(SpecialityMapper specialityMapper) {
        this.specialityMapper = specialityMapper;
    }


}

