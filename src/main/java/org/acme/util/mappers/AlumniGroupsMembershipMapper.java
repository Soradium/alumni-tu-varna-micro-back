package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.AlumniGroupsMembershipDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;

@ApplicationScoped
public class AlumniGroupsMembershipMapper {

    public AlumniGroupsMembershipDto toDto(AlumniGroupsMembership entity) {
        AlumniGroupsMembershipDto dto = new AlumniGroupsMembershipDto();
        dto.setId(entity.getId());
        dto.setFacultyNumber(entity.getAlumni().getFacultyNumber());
        if(entity.getGroup()!=null) {
            dto.setGroupNumber(entity.getGroup().getGroupNumber());
        }
        dto.setAverageScore(entity.getAverageScore());
        return dto;
    }

    public AlumniGroupsMembership fromDto(AlumniGroupsMembershipDto dto,
                                          Alumni alumni, AlumniGroup group) {
        AlumniGroupsMembership entity = new AlumniGroupsMembership();
        entity.setAlumni(alumni);
        entity.setGroup(group);
        entity.setAverageScore(dto.getAverageScore());
        return entity;
    }
}
