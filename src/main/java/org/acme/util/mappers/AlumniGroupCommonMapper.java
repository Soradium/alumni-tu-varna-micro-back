package org.acme.util.mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.acme.avro.ambiguous.AlumniGroupDtoSimplified;
import org.acme.avro.ambiguous.FacultyDto;
import org.acme.avro.ambiguous.SpecialityDto;
import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.avro.front.AlumniGroupMembershipFrontDto;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import jakarta.inject.Inject;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA,
        uses = {AlumniGroupMapper.class, AlumniGroupMembershipMapper.class})
public abstract class AlumniGroupCommonMapper {

    @Inject
    protected AlumniGroupMapper groupMapper;

    @Inject
    protected AlumniGroupMembershipMapper membershipMapper;

    // --- MEMBERSHIP MAPPERS ---

@Named("AlumniGroupMembershipToBackDto")
public AlumniGroupMembershipDto toBackAlumniGroupMembershipDto(AlumniGroupsMembership entity) {
    if (entity == null) return null;

    AlumniGroupMembershipDto.Builder builder = AlumniGroupMembershipDto.newBuilder();

    builder.setId(entity.getId());

    builder.setFacultyNumber(entity.getAlumni() != null
        ? entity.getAlumni().getFacultyNumber()
        : null);

    builder.setGroup(entity.getGroup() != null
        ? AlumniGroupBackDto.newBuilder()
            .setId(entity.getGroup().getId())
            .setGroupNumber(entity.getGroup().getGroupNumber())
            .setGraduationYear(entity.getGroup().getGraduationYear())
            .setFaculty(entity.getGroup().getFaculty() != null
                ? FacultyDto.newBuilder()
                        .setId(entity.getGroup().getFaculty().getId())
                        .setFacultyName(entity.getGroup().getFaculty().getFacultyName())
                        .build()
                : new FacultyDto())
            .setSpeciality(entity.getGroup().getSpeciality() != null
                ? SpecialityDto.newBuilder()
                        .setId(entity.getGroup().getSpeciality().getId())
                        .setSpecialityName(entity.getGroup().getSpeciality().getSpecialityName())
                        .build()
                : new SpecialityDto())
            .setMembershipIds(entity.getGroup().getMemberships() != null
                ? entity.getGroup().getMemberships().stream()
                        .map(AlumniGroupsMembership::getId)
                        .collect(Collectors.toList())
                : Collections.emptyList())
            .build()
        : null);

    builder.setAverageScore(entity.getAverageScore() != 0 ? entity.getAverageScore() : null);

    return builder.build();
}


    @Named("toBackDtoSingle")
    public AlumniGroupMembershipDto toBackDtoWithMembershipIds(AlumniGroupsMembership entity) {
        AlumniGroupMembershipDto dto = this.toBackAlumniGroupMembershipDto(entity);
        if (entity.getGroup() != null) {
            dto.setGroup(this.toAlumniGroupDto(entity.getGroup()));
        }
        return dto;
    }

    @Named("toEntitySingle")
    public AlumniGroupsMembership toEntity(AlumniGroupMembershipDto dto) {
        AlumniGroupsMembership agm = membershipMapper.toEntity(dto);
        if (dto.getGroup() != null) {
            agm.setGroup(groupMapper.toEntity(dto.getGroup()));
        }
        return agm;
    }

    @Named("toEntityFrontSingle")
    public AlumniGroupsMembership toEntityFront(AlumniGroupMembershipFrontDto dto) {
        return membershipMapper.toEntityFront(dto);
    }

    @Named("toEntityFromDtoAlumniGroupMembershipList")
    @IterableMapping(qualifiedByName = "toEntitySingle")
    public abstract List<AlumniGroupsMembership> toEntityList(List<AlumniGroupMembershipDto> dtos);

    @Named("toEntityFromDtoAlumniGroupMembershipFrontList")
    @IterableMapping(qualifiedByName = "toEntityFrontSingle")
    public abstract List<AlumniGroupsMembership> toEntityFrontList(List<AlumniGroupMembershipFrontDto> dtos);

    @Named("toBackDtoListAlumniGroupMembership")
    @IterableMapping(qualifiedByName = "toBackDtoSingle")
    public abstract List<AlumniGroupMembershipDto> toBackDtos(List<AlumniGroupsMembership> memberships);

    @Named("extractMembershipIds")
    public List<Integer> extractMembershipIds(List<AlumniGroupsMembership> memberships) {
        if (memberships == null) return java.util.Collections.emptyList();
        return memberships.stream()
            .map(AlumniGroupsMembership::getId)
            .collect(Collectors.toList());
    }

    @Named("toBasicEntity")
    @IterableMapping(qualifiedByName = "toBasicEntitySingle")
    public abstract List<AlumniGroupsMembership> toBasicEntity(List<Integer> membershipIds);

    // --- GROUP MAPPERS ---

    
    @Mapping(
        target = "membershipIds",
        expression = "java(extractMembershipIds(alumniGroup.getMemberships()))"
    )
    @Named("ToAlumniGroupBackDto")
    public AlumniGroupBackDto toAlumniGroupDto(AlumniGroup alumniGroup) {
            if (alumniGroup == null) return null;

        AlumniGroupBackDto.Builder builder = AlumniGroupBackDto.newBuilder();

        builder.setId(alumniGroup.getId());
        builder.setGroupNumber(alumniGroup.getGroupNumber());
        builder.setGraduationYear(alumniGroup.getGraduationYear());

        builder.setFaculty(alumniGroup.getFaculty() != null
            ? FacultyDto.newBuilder()
                        .setId(alumniGroup.getFaculty().getId())
                        .setFacultyName(alumniGroup.getFaculty().getFacultyName())
                        .build()
            : new FacultyDto());

        builder.setSpeciality(alumniGroup.getSpeciality() != null
            ? SpecialityDto.newBuilder()
                        .setId(alumniGroup.getSpeciality().getId())
                        .setSpecialityName(alumniGroup.getSpeciality().getSpecialityName())
                        .build()
            : new SpecialityDto());

        builder.setMembershipIds(alumniGroup.getMemberships() != null
            ? alumniGroup.getMemberships().stream()
                        .map(AlumniGroupsMembership::getId)
                        .collect(Collectors.toList())
            : Collections.emptyList());

        return builder.build();
    }




    public AlumniGroup toEntity(AlumniGroupBackDto dto) {
        AlumniGroup g = groupMapper.toEntity(dto);
        List<AlumniGroupsMembership> memberships = toBasicEntity(dto.getMembershipIds());
        memberships.forEach(m -> m.setGroup(g));
        g.setMemberships(memberships);
        return g;
    }

    public AlumniGroup toEntitySimplified(AlumniGroupDtoSimplified dto) {
        return groupMapper.toEntitySimplified(dto);
    }

    @IterableMapping(qualifiedByName = "toAlumniGroupBackDtoList")
    public List<AlumniGroupBackDto> toDtoList(List<AlumniGroup> list) {
        return list.stream().map(this::toAlumniGroupDto).collect(Collectors.toList());
    }


}
