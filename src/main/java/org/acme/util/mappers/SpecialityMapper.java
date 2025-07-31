package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.SpecialityDto;
import org.acme.entites.Speciality;

@ApplicationScoped
public class SpecialityMapper {
    public SpecialityDto toDto(Speciality speciality){
        SpecialityDto dto = new SpecialityDto();
        dto.setId(speciality.getId());
        dto.setSpeciality(speciality.getSpeciality());
        return dto;
    }

    public Speciality toEntity(SpecialityDto dto){
        Speciality specialities = new Speciality();
        specialities.setSpecialityName(dto.getSpeciality());
        return specialities;
    }
}
