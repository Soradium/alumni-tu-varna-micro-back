package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.FacultyDto;
import org.acme.entites.Faculty;

@ApplicationScoped
public class FacultyMapper {
    public FacultyDto toDto(Faculty faculty){
        FacultyDto facultyDto = new FacultyDto();
        facultyDto.setId(faculty.getId());
        facultyDto.setFacultyName(faculty.getFacultyName());
        return facultyDto;
    }

    public Faculty toEntity(FacultyDto facultyDto){
        Faculty faculty = new Faculty();
        faculty.setFacultyName(facultyDto.getFacultyName());
        return faculty;
    }
}
