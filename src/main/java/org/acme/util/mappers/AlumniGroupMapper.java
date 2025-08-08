package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.avro.ambiguous.AlumniGroupDtoSimplified;
import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.entites.AlumniGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi", uses = {
        FacultyMapper.class, SpecialityMapper.class, AlumniGroupMembershipMapper.class
})
@ApplicationScoped
public abstract class AlumniGroupMapper {

    @Inject
    private FacultyMapper facultyMapper;
    @Inject
    private SpecialityMapper specialityMapper;
    @Inject
    private AlumniGroupMembershipMapper groupMembershipMapper;

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

    public List<AlumniGroupBackDto> toDtoList(List<AlumniGroup> list) {
        return list.stream().map(m -> {
            AlumniGroupBackDto dto = new AlumniGroupBackDto();

            dto.setId(dto.getId());
            dto.setGroupNumber(dto.getGroupNumber());
            dto.setGraduationYear(dto.getGraduationYear());
            dto.setFaculty(facultyMapper.toDto(m.getFaculty()));
            dto.setSpeciality(specialityMapper.toDto(m.getSpeciality()));
            dto.setMembershipIds((ArrayList) groupMembershipMapper.toBackDtos(m.getMemberships()));

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


}

