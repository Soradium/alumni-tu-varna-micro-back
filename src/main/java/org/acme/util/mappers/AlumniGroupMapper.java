package org.acme.util.mappers;

import java.util.ArrayList;

import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.entites.AlumniGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
@Mapper(componentModel = "cdi", uses = { 
    FacultyMapper.class, SpecialityMapper.class, AlumniGroupMembershipMapper.class
})
@ApplicationScoped
public abstract class AlumniGroupMapper {

    
    private final FacultyMapper facultyMapper;
    private final SpecialityMapper specialityMapper;
    private final AlumniGroupMembershipMapper groupMembershipMapper;

    @Inject
    public AlumniGroupMapper(
            FacultyMapper facultyMapper, 
            SpecialityMapper specialityMapper, 
            AlumniGroupMembershipMapper groupMembershipMapper) {
        this.facultyMapper = facultyMapper;
        this.specialityMapper = specialityMapper;
        this.groupMembershipMapper = groupMembershipMapper;
    }

    @Mapping(source = "memberships", target = "membershipIds", qualifiedByName = "extractMembershipIds")
    public abstract AlumniGroupBackDto toDto(AlumniGroup entity);

    @Named("toAlumniGroupEntity")
    public AlumniGroup toEntity(AlumniGroupBackDto dto) {
        AlumniGroup entity = new AlumniGroup();

        entity.setId(dto.getId());
        entity.setGroupNumber(dto.getGroupNumber());
        entity.setGraduationYear(dto.getGraduationYear());
        entity.setFaculty(facultyMapper.toEntity(dto.getFaculty()));
        entity.setSpeciality(specialityMapper.toEntity(dto.getSpeciality()));
        entity.setMemberships((ArrayList) groupMembershipMapper.toBasicEntity(dto.getMembershipIds())); // will probably need enrichment, added by ids

        return entity;
    }

}

