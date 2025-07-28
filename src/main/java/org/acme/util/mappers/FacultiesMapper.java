package org.acme.util.mappers;

import org.acme.dto.FacultyDto;
import org.acme.entites.Faculties;

public class FacultiesMapper {
    public FacultyDto toDto(Faculties faculty){
        FacultyDto facultyDto = new FacultyDto();
        facultyDto.setId(faculty.getId());
        facultyDto.setFacultyName(faculty.getFacultyName());
        return facultyDto;
    }

    public Faculties toEntity(FacultyDto facultyDto){
        Faculties faculty = new Faculties();
        faculty.setFacultyName(facultyDto.getFacultyName());
        return faculty;
    }
}
