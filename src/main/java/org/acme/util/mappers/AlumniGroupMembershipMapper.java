package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.avro.front.AlumniGroupMembershipFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi", uses = {
        AlumniGroupMapper.class

}
)
@ApplicationScoped
public abstract class AlumniGroupMembershipMapper {

    @Inject
    private AlumniGroupMapper groupMapper;

    @Mapping(target = "facultyNumber", source = "alumni.facultyNumber")
    @Mapping(target = "groupBuilder", ignore = true)
    public abstract AlumniGroupMembershipDto toBackDto(AlumniGroupsMembership entity);

    @Named("toEntityFromDtoAlumniGroupMembershipList")
    public List<AlumniGroupsMembership> toEntityList(
            List<AlumniGroupMembershipDto> membershipDtoList) {
        return membershipDtoList != null
                ? membershipDtoList.stream().map(m -> {
            AlumniGroupsMembership agm = new AlumniGroupsMembership();
            agm.setId(m.getId());

            Alumni alumni = new Alumni();
            alumni.setFacultyNumber(m.getFacultyNumber());
            agm.setAlumni(alumni); // will probably need enrichment or wiring, only faculty number added

            agm.setAverageScore(m.getAverageScore());
            agm.setGroup(groupMapper.toEntity(m.getGroup()));

            return agm;
        }).collect(Collectors.toList())
                : new ArrayList<>();
    }

    @Named("toEntityFromDtoAlumniGroupMembershipFrontList")
    public List<AlumniGroupsMembership> toEntityFrontList(
            List<AlumniGroupMembershipFrontDto> membershipDtoList) {
        return membershipDtoList != null
                ? membershipDtoList.stream().map(m -> {
            AlumniGroupsMembership agm = new AlumniGroupsMembership();
            agm.setId(m.getId());

            Alumni alumni = new Alumni();
            alumni.setFacultyNumber(m.getFacultyNumber());
            agm.setAlumni(alumni); // will probably need enrichment or wiring, only faculty number added

            agm.setAverageScore(m.getAverageScore());
            AlumniGroup group = new AlumniGroup();
            group.setId(m.getGroupNumber());
            agm.setGroup(group);

            return agm;
        }).collect(Collectors.toList())
                : new ArrayList<>();
    }

    @Named("toEntityFromDtoAlumniGroupMembershipSingle")
    public AlumniGroupsMembership toEntity(
            AlumniGroupMembershipDto dto
    ) {
        AlumniGroupsMembership agm = new AlumniGroupsMembership();
        agm.setId(dto.getId());

        Alumni alumni = new Alumni();
        alumni.setFacultyNumber(dto.getFacultyNumber());
        agm.setAlumni(alumni); // will probably need enrichment or wiring, only faculty number added

        agm.setAverageScore(dto.getAverageScore());
        agm.setGroup(groupMapper.toEntity(dto.getGroup()));

        return agm;

    }


    @Named("toEntityFromDtoAlumniGroupMembershipFrontSingle")
    public AlumniGroupsMembership toEntityFront(
            AlumniGroupMembershipFrontDto dto
    ) {
        AlumniGroupsMembership agm = new AlumniGroupsMembership();
        agm.setId(dto.getId());

        Alumni alumni = new Alumni();
        alumni.setFacultyNumber(dto.getFacultyNumber());
        agm.setAlumni(alumni); // will probably need enrichment or wiring, only faculty number added

        agm.setAverageScore(dto.getAverageScore());
        AlumniGroup group = new AlumniGroup();
        group.setId(dto.getGroupNumber());
        agm.setGroup(group);
        return agm;

    }

    @Named("toBackDtoListAlumniGroupMembership")
    public List<AlumniGroupMembershipDto> toBackDtos(List<AlumniGroupsMembership> memberships) {
        return memberships.stream().map(m -> {
            AlumniGroupMembershipDto dto = new AlumniGroupMembershipDto();
            dto.setAverageScore(m.getAverageScore());
            dto.setFacultyNumber(m.getAlumni().getFacultyNumber());
            dto.setGroup(groupMapper.toDto(m.getGroup()));
            dto.setId(m.getId());
            return dto;
        }).collect(Collectors.toList());
    }


    @Named("extractMembershipIds")
    public List<Integer> extractMembershipIds(List<AlumniGroupsMembership> memberships) {
        return memberships != null
                ? memberships.stream().map(m -> m.id).collect(Collectors.toList())
                : new ArrayList<>();
    }

    @Named("toBasicEntity")
    public List<AlumniGroupsMembership> toBasicEntity(List<Integer> membershipIds) {
        return membershipIds != null
                ? membershipIds.stream().map(m -> {
            AlumniGroupsMembership agm = new AlumniGroupsMembership();
            agm.id = m;
            return agm;
        }).collect(Collectors.toList())
                : new ArrayList<>();
    }


}
