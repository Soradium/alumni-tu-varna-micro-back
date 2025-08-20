package org.acme.util.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.avro.front.AlumniGroupMembershipFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA, uses = {
}
)
public abstract class AlumniGroupMembershipMapper {

    @Named("toEntityFromDtoAlumniGroupMembershipList")
    public List<AlumniGroupsMembership> toEntityList(
            List<AlumniGroupMembershipDto> membershipDtoList) {
        return membershipDtoList != null
                ? membershipDtoList.stream().map(m -> {
            AlumniGroupsMembership agm = new AlumniGroupsMembership();
            agm.setId(m.getId());

            Alumni alumni = new Alumni();
            alumni.setFacultyNumber(m.getFacultyNumber());
            agm.setAlumni(alumni); 
            // will probably need enrichment or wiring, only faculty number added
            // they do not have group also
            agm.setAverageScore(m.getAverageScore());

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
            agm.setAlumni(alumni); 
            // will probably need enrichment or wiring, only faculty number added
            // does not have real group also

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
            AlumniGroupMembershipDto dto) {
        AlumniGroupsMembership agm = new AlumniGroupsMembership();
        agm.setId(dto.getId());

        Alumni alumni = new Alumni();
        alumni.setFacultyNumber(dto.getFacultyNumber());
        agm.setAlumni(alumni); 
        // will probably need enrichment or wiring, only faculty number added

        agm.setAverageScore(dto.getAverageScore());

        return agm;

    }

    @Named("toEntityFromDtoAlumniGroupMembershipFrontSingle")
    public AlumniGroupsMembership toEntityFront(
            AlumniGroupMembershipFrontDto dto) {
        AlumniGroupsMembership agm = new AlumniGroupsMembership();
        agm.setId(dto.getId());

        Alumni alumni = new Alumni();
        alumni.setFacultyNumber(dto.getFacultyNumber());
        agm.setAlumni(alumni); 
        // will probably need enrichment or wiring, only faculty number added

        agm.setAverageScore(dto.getAverageScore());
        AlumniGroup group = new AlumniGroup();
        // will need group enrichment

        group.setId(dto.getGroupNumber());
        agm.setGroup(group);
        return agm;

    }

    @Named("toBackDtoListAlumniGroupMembership")
    public List<AlumniGroupMembershipDto> toBackDtos(List<AlumniGroupsMembership> memberships) {
        return memberships.stream().map(m -> {
            AlumniGroupMembershipDto dto = new AlumniGroupMembershipDto();
            // will need group enrichment
            dto.setAverageScore(m.getAverageScore());
            dto.setFacultyNumber(m.getAlumni().getFacultyNumber());
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

     @Named("extractMembershipId")
    public Integer extractMembershipId(AlumniGroupsMembership membership) {
        return membership != null ? membership.getId() : null;
    }

    @Named("toBasicEntitySingle")
    public AlumniGroupsMembership toBasicEntitySingle(Integer membershipId) {
        if (membershipId == null) return null;
        AlumniGroupsMembership agm = new AlumniGroupsMembership();
        agm.setId(membershipId);
        return agm;
    }
    

}
