package org.acme.service;

import java.util.List;

import org.acme.avro.ambiguous.FacultyDto;
import org.acme.entites.Faculty;

public interface FacultyService {
    Faculty getFacultyById(long id) throws Exception;
    Faculty getFacultyByName(String name) throws Exception;
    List<FacultyDto> getAllFaculties() throws Exception;

    Faculty createFaculty(FacultyDto facultyDto) throws Exception;
    Faculty updateFaculty(FacultyDto facultyDto) throws Exception;
    void deleteFaculty(long id) throws Exception;

}
