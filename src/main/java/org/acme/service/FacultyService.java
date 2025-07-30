package org.acme.service;

import org.acme.dto.FacultyDto;
import org.acme.entites.Faculty;

import java.util.List;

public interface FacultyService {
    FacultyDto getFacultyById(long id);
    Faculty getFacultyByIdE(long id);
    Faculty getFacultyByNameE(String name);
    FacultyDto getFacultyByName(String name);
    List<FacultyDto> getAllFaculties();

    FacultyDto createFaculty(FacultyDto faculty);
    FacultyDto updateFaculty(long id, FacultyDto faculty);
    FacultyDto deleteFaculty(long id);

    boolean isFacultyExist(String facultyName);
}
