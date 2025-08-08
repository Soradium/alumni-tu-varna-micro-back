package org.acme.service.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.avro.ambiguous.FacultyDto;
import org.acme.entites.Faculty;
import org.acme.repository.FacultyRepository;
import org.acme.service.FacultyService;
import org.acme.util.mappers.FacultyMapper;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;

    @Inject
    public FacultyServiceImpl(FacultyRepository facultyRepository, FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
    }

    @Override
    public Faculty createFaculty(FacultyDto facultyDto) throws Exception {
        if (facultyDto == null) {
            throw new Exception("FacultyDto is null.");
        }
        Faculty faculty = facultyMapper.toEntity(facultyDto);
        facultyRepository.persist(faculty);
        return faculty;
    }

    @Override
    public void deleteFaculty(long id) throws Exception {
        if (id <= 0) {
            throw new Exception("Faculty ID is invalid.");
        }
        boolean deleted = facultyRepository.deleteById(id);
        if (!deleted) {
            throw new Exception("Faculty with ID " + id + " does not exist.");
        }
    }

    @Override
    public List<FacultyDto> getAllFaculties() throws Exception {
        List<Faculty> faculties = facultyRepository.findAll().list();
        return convertToDtoList(faculties);
    }

    @Override
    public Faculty getFacultyById(long id) throws Exception {
        if (id <= 0) {
            throw new Exception("Faculty ID is invalid.");
        }
        return facultyRepository.findById(id);
    }

    @Override
    public Faculty getFacultyByName(String name) throws Exception {
        if (name == null || name.isEmpty()) {
            throw new Exception("Faculty name is null or empty.");
        }
        return facultyRepository.findByNameOptional(name)
                .orElseThrow(() -> new Exception("Faculty with name '" + name + "' not found."));
    }

    @Override
    public Faculty updateFaculty(FacultyDto facultyDto) throws Exception {
        if (facultyDto == null) {
            throw new Exception("FacultyDto is null.");
        }

        Faculty existingFaculty = facultyRepository
                .findByIdOptional((long) facultyDto.getId())
                .orElseThrow(() -> new Exception("No faculty with such ID was found."));

        existingFaculty.setFacultyName(facultyDto.getFacultyName());
        facultyRepository.persist(existingFaculty);
        return existingFaculty;
    }

    public List<FacultyDto> convertToDtoList(List<Faculty> faculties) {
        return faculties.stream()
                .map(f -> new FacultyDto(f.getId(), f.getFacultyName()))
                .collect(Collectors.toList());
    }
}
