package org.acme.service;

import org.acme.dto.FacultyDto;
import org.acme.entites.Faculties;

import java.util.List;

public interface FacultiesService {
    FacultyDto getFacultyById(long id);
    FacultyDto getFacultyByName(String name);
    List<FacultyDto> getAllFaculties();

    FacultyDto createFaculty(FacultyDto faculty);
    FacultyDto updateFaculty(long id, FacultyDto faculty);
    FacultyDto deleteFaculty(long id);

    boolean isFacultyExist(String facultyName);
}
