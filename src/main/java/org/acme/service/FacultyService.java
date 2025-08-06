package org.acme.service;

import java.util.List;

import org.acme.avro.ambiguous.FacultyDto;
import org.acme.entites.Faculty;

public interface FacultyService {
    Faculty getFacultyById(long id);
    Faculty getFacultyByName(String name);
    List<FacultyDto> getAllFaculties();

    Faculty createFaculty(FacultyDto facultyDto);
    Faculty updateFaculty(FacultyDto facultyDto);
    void deleteFaculty(long id);

    FacultyDto convertFacultyToDto(Faculty faculty);
    Faculty convertFacultyFromDto(FacultyDto dto);
}
