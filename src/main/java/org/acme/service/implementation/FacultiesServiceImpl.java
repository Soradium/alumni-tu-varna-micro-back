package org.acme.service.implementation;

import org.acme.dto.FacultyDto;
import org.acme.entites.Faculties;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.FacultiesRepository;
import org.acme.service.FacultiesService;
import org.acme.util.mappers.FacultiesMapper;

import java.util.List;
import java.util.stream.Collectors;

public class FacultiesServiceImpl implements FacultiesService {

    private final FacultiesRepository facultiesRepository;
    private final FacultiesMapper facultiesMapper;

    public FacultiesServiceImpl(FacultiesRepository facultiesRepository, FacultiesMapper facultiesMapper) {
        this.facultiesRepository = facultiesRepository;
        this.facultiesMapper = facultiesMapper;
    }

    @Override
    public FacultyDto getFacultyById(long id) {
        //TODO: check id
        Faculties faculty = facultiesRepository.findByIdOptional(id)
                .orElseThrow(() -> new RuntimeException("Faculty with ID: " +
                      id + " not found"));
        return facultiesMapper.toDto(faculty);
    }

    @Override
    public FacultyDto getFacultyByName(String name) {
        Faculties faculty = facultiesRepository.findByNameOptional(name)
                .orElseThrow(() -> new RuntimeException("Faculty " +
                        name + " not found"));
        return facultiesMapper.toDto(faculty);
    }

    @Override
    public List<FacultyDto> getAllFaculties() {
        List<Faculties> facultiesList = facultiesRepository.listAll();
        if(facultiesList.isEmpty()) {
            throw new ResourceNotFoundException("No Faculties found");
        }
        return facultiesList.stream().map(facultiesMapper::toDto).toList();
    }

    @Override
    public FacultyDto createFaculty(FacultyDto faculty) {
        Faculties f = facultiesMapper.toEntity(faculty);
        facultiesRepository.persist(f);
        return facultiesMapper.toDto(f);
    }

    @Override
    public FacultyDto updateFaculty(long id, FacultyDto faculty) {
        Faculties dbFaculty = facultiesRepository.findByIdOptional(id)
                .orElseThrow(() -> new RuntimeException("Faculty with ID: " +
                        id + " not found"));
        dbFaculty.setFacultyName(faculty.getFacultyName());
        facultiesRepository.persist(dbFaculty);
        return facultiesMapper.toDto(dbFaculty);
    }

    @Override
    public FacultyDto deleteFaculty(long id) {
        Faculties dbFaculty = facultiesRepository.findByIdOptional(id)
                .orElseThrow(() -> new RuntimeException("Faculty with ID: " +
                        id + " not found"));
        facultiesRepository.delete(dbFaculty);
        return facultiesMapper.toDto(dbFaculty);
    }

    @Override
    public boolean isFacultyExist(String facultyName) {
        Faculties faculty = facultiesRepository.findByNameOptional(facultyName)
                .orElseThrow(() -> new RuntimeException("Faculty " +
                        facultyName + " not found"));
        if(faculty != null) {
            return true;
        }
        return false;
    }
}
