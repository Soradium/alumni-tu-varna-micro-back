package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;

import org.acme.avro.AlumniGroupDto;
import org.acme.avro.AlumniGroupMembershipDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;

@ApplicationScoped
public class AlumniGroupsMembershipMapper {

    public AlumniGroupMembershipDto toDto(AlumniGroupsMembership entity) {
        AlumniGroupMembershipDto dto = new AlumniGroupMembershipDto();
        dto.setId(entity.getId());
        dto.setFacultyNumber(entity.getAlumni().getFacultyNumber());
        if(entity.getGroup()!=null) {
            AlumniGroupDto gDto = new AlumniGroupDto();
            gDto.setGroupNumber(entity.getGroup().getGroupNumber());
            gDto.setGraduationYear(entity.getGroup().getGraduationYear());
            gDto.setFaculty(entity.getGroup().getFaculties().getFacultyName());
            gDto.setSpecialityName(entity.getGroup().getSpeciality().getSpecialityName());
            dto.setGroup(gDto);
        }
        dto.setAverageScore(entity.getAverageScore());
        return dto;
    }

    public AlumniGroupsMembership fromDto(AlumniGroupMembershipDto dto,
                                          Alumni alumni, AlumniGroup group) {
        AlumniGroupsMembership entity = new AlumniGroupsMembership();
        entity.setAlumni(alumni);
        entity.setGroup(group);
        entity.setAverageScore(dto.getAverageScore());
        return entity;
    }
}
