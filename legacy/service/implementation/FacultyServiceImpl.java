package org.acme.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.FacultyDto;
import org.acme.entites.Faculty;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.FacultyRepository;
import org.acme.service.FacultyService;
import org.acme.util.mappers.FacultyMapper;

import java.util.List;

@ApplicationScoped
public class FacultyServiceImpl implements FacultyService {

    @Inject
    FacultyRepository facultyRepository;
    @Inject
    FacultyMapper facultyMapper;

    @Override
    public FacultyDto getFacultyById(long id) {
        //TODO: check id
        Faculty faculty = facultyRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty with ID: " +
                      id + " not found"));
        return facultyMapper.toDto(faculty);
    }

    @Override
    public Faculty getFacultyByIdE(long id) {
        Faculty faculty = facultyRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty with ID: " +
                        id + " not found"));
        return faculty;
    }

    @Override
    public Faculty getFacultyByNameE(String name) {
        Faculty faculty = facultyRepository.findByNameOptional(name)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty " +
                        name + " not found"));
        return faculty;
    }

    @Override
    public FacultyDto getFacultyByName(String name) {
        Faculty faculty = facultyRepository.findByNameOptional(name)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty " +
                        name + " not found"));
        return facultyMapper.toDto(faculty);
    }

    @Override
    public List<FacultyDto> getAllFaculties() {
        List<Faculty> facultiesList = facultyRepository.listAll();
        if(facultiesList.isEmpty()) {
            throw new ResourceNotFoundException("No faculties found");
        }
        return facultiesList.stream().map(facultyMapper::toDto).toList();
    }

    @Override
    @Transactional
    public FacultyDto createFaculty(FacultyDto faculty) {
        Faculty f = facultyMapper.toEntity(faculty);
        facultyRepository.persist(f);
        return facultyMapper.toDto(f);
    }

    @Override
    @Transactional
    public FacultyDto updateFaculty(long id, FacultyDto faculty) {
        Faculty dbFaculty = facultyRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty with ID: " +
                        id + " not found"));
        dbFaculty.setFacultyName(faculty.getFacultyName());
        facultyRepository.persist(dbFaculty);
        return facultyMapper.toDto(dbFaculty);
    }

    @Override
    @Transactional
    public FacultyDto deleteFaculty(long id) {
        Faculty dbFaculty = facultyRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty with ID: " +
                        id + " not found"));
        facultyRepository.delete(dbFaculty);
        return facultyMapper.toDto(dbFaculty);
    }

    @Override
    public boolean isFacultyExist(String facultyName) {
        Faculty faculty = facultyRepository.findByNameOptional(facultyName)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty " +
                        facultyName + " not found"));
        if(faculty != null) {
            return true;
        }
        return false;
    }
}
