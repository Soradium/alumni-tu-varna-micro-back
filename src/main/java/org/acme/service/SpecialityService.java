package org.acme.service;

import java.util.List;

import org.acme.avro.ambiguous.SpecialityDto;
import org.acme.entites.Speciality;

public interface SpecialityService {
    Speciality getSpecialityById(long id);
    Speciality getSpecialityByName(String name);
    List<SpecialityDto> getAllSpecialities();

    Speciality createSpeciality(SpecialityDto specialityDto);
    Speciality updateSpeciality(SpecialityDto specialityDto);
    void deleteSpeciality(long id);

    SpecialityDto convertSpecialityToDto(Speciality speciality);
    Speciality convertSpecialityFromDto(SpecialityDto dto);
}
