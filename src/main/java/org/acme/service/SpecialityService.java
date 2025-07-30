package org.acme.service;

import org.acme.dto.SpecialityDto;
import org.acme.entites.Speciality;

import java.util.List;

public interface SpecialityService {
    SpecialityDto getSpecialityById(long id);
    Speciality getSpecialityByNameE(String name);
    SpecialityDto getSpecialityByName(String name);
    List<SpecialityDto> getAllSpecialities();

    SpecialityDto createSpeciality(SpecialityDto specialityDto);
    SpecialityDto updateSpeciality(long id, SpecialityDto specialityDto);
    SpecialityDto deleteSpeciality(long id);

    boolean checkSpeciality(String specialityName);
}
