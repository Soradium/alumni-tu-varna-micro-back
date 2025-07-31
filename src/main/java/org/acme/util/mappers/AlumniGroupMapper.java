package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.dto.AlumniGroupDto;
import org.acme.dto.AlumniGroupsMembershipDto;
import org.acme.entites.*;
import org.acme.repository.FacultyRepository;
import org.acme.repository.SpecialityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AlumniGroupMapper {
    @Inject
    public FacultyMapper facultyMapper;
    @Inject
    public SpecialityMapper specialityMapper;
    @Inject
    public AlumniGroupsMembershipMapper membershipMapper;

    public AlumniGroupDto toDto(AlumniGroup alumniGroup) {
        AlumniGroupDto dto = new AlumniGroupDto();
        dto.setId(alumniGroup.getId());
        dto.setFaculty(facultyMapper.toDto(alumniGroup.getFaculty()));
        dto.setGroupNumber(alumniGroup.getGroupNumber());
        dto.setGraduationYear(alumniGroup.getGraduationYear());
        dto.setSpeciality(specialityMapper.toDto(alumniGroup.getSpeciality()));
        dto.setMemberships(
                alumniGroup.getMemberships().stream().map(membershipMapper::toDto)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
        return dto;
    }

    public AlumniGroup toEntity(AlumniGroupDto dto, Faculty faculty,
                                Speciality speciality,
                                List<AlumniGroupsMembership> memberships) {
        AlumniGroup entity = new AlumniGroup();
        entity.setFaculty(faculty);
        entity.setGroupNumber(dto.getGroupNumber());
        entity.setGraduationYear(dto.getGraduationYear());
        entity.setSpeciality(speciality);
        entity.setMemberships(new ArrayList<>(memberships));
        return entity;
    }

}
