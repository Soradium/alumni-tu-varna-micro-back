package org.acme.util.mappers;

import org.acme.dto.SpecialityDto;
import org.acme.entites.Specialities;

public class SpecialitiesMapper {
    public SpecialityDto toDto(Specialities speciality){
        SpecialityDto dto = new SpecialityDto();
        dto.setId(speciality.getId());
        dto.setSpeciality(speciality.getSpeciality());
        return dto;
    }

    public Specialities toEntity(SpecialityDto dto){
        Specialities specialities = new Specialities();
        specialities.setSpeciality(dto.getSpeciality());
        return specialities;
    }
}
