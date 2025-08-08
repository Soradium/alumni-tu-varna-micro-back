package org.acme.service;

import java.util.List;

import org.acme.avro.ambiguous.SpecialityDto;
import org.acme.entites.Speciality;

public interface SpecialityService {
    Speciality getSpecialityById(long id) throws Exception;
    Speciality getSpecialityByName(String name) throws Exception;
    List<SpecialityDto> getAllSpecialities() throws Exception;

    Speciality createSpeciality(SpecialityDto specialityDto) throws Exception;
    Speciality updateSpeciality(SpecialityDto specialityDto) throws Exception;
    void deleteSpeciality(long id) throws Exception;

}
