package org.acme.util.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniGroupsMembership;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Mapper(componentModel = "cdi", uses = {
        AlumniGroupMapper.class, 
        SimpleAlumniMapper.class,
    }
)
@ApplicationScoped
public abstract class AlumniGroupMembershipMapper {

    private final AlumniGroupMapper groupMapper;

    @Inject
    public AlumniGroupMembershipMapper(AlumniGroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Mapping(target = "facultyNumber", source = "alumni.facultyNumber")
    public abstract AlumniGroupMembershipDto toBackDto(AlumniGroupsMembership entity);
    
    @Named("toEntityFromDtoAlumniGroup")
    public List<AlumniGroupsMembership> toEntity(
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
