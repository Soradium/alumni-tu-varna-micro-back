package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.dto.AlumniGroupsMembershipDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;
import org.acme.repository.AlumniGroupRepository;
import org.acme.repository.AlumniRepository;

@ApplicationScoped
public class AlumniGroupsMembershipMapper {
    @Inject
    AlumniRepository alumniRepository;
    @Inject
    AlumniGroupRepository alumniGroupRepository;

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
