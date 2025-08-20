package org.acme.util.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.acme.avro.ambiguous.AlumniGroupDtoSimplified;
import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import jakarta.inject.Inject;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA, uses = {
        FacultyMapper.class, SpecialityMapper.class
})
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

    @Named("toAlumniGroupBackDtoList")
    public List<AlumniGroupBackDto> toDtoList(List<AlumniGroup> list) {
        return list.stream().map(m -> {
            AlumniGroupBackDto dto = new AlumniGroupBackDto();

            dto.setId(dto.getId());
            dto.setGroupNumber(dto.getGroupNumber());
            dto.setGraduationYear(dto.getGraduationYear());
            dto.setFaculty(facultyMapper.toDto(m.getFaculty()));
            dto.setSpeciality(specialityMapper.toDto(m.getSpeciality()));
            dto.setMembershipIds(m.getMemberships().stream().map(k -> {
                return k.getId();
            }).collect(Collectors.toList()));

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

    @Named("extractMembershipIdsLocal")
    protected List<Integer> extractMembershipIdsLocal(List<AlumniGroupsMembership> memberships) {
    return memberships != null
            ? memberships.stream().map(AlumniGroupsMembership::getId).collect(Collectors.toList())
            : new ArrayList<>();
    }



}




    // @Named("ToAlumniGroupBackDto")
    // public AlumniGroupBackDto toAlumniGroupDto(AlumniGroup entity) {
    //     AlumniGroupBackDto dto = new AlumniGroupBackDto();
    //     dto.setFaculty(facultyMapper.toDto(entity.getFaculty()));
    //     dto.setGroupNumber(entity.getGroupNumber());
    //     dto.setGraduationYear(entity.getGraduationYear());
    //     dto.setId(entity.getId());
    //     dto.setSpeciality(specialityMapper.toDto(entity.getSpeciality()));
    //     dto.setMembershipIds(entity.getMemberships().stream().map(k -> {
    //         return k.getId();
    //     }).collect(Collectors.toList()));
    //     return dto;
    // }
