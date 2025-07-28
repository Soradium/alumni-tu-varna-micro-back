package org.acme.service;

import org.acme.dto.SpecialityDto;

import java.util.List;

public interface SpecialitiesService {
    SpecialityDto getSpecialityById(long id);
    SpecialityDto getSpecialityByName(String name);
    List<SpecialityDto> getAllSpecialities();

    SpecialityDto createSpeciality(SpecialityDto specialityDto);
    SpecialityDto updateSpeciality(long id, SpecialityDto specialityDto);
    SpecialityDto deleteSpeciality(long id);

    boolean checkSpeciality(String specialityName);
}
