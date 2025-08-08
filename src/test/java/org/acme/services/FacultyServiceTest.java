package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.acme.avro.ambiguous.FacultyDto;
import org.acme.entites.Faculty;
import org.acme.repository.FacultyRepository;
import org.acme.service.implementations.FacultyServiceImpl;
import org.acme.util.mappers.FacultyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private FacultyMapper facultyMapper;

    @InjectMocks
    private FacultyServiceImpl facultyService;

    private FacultyDto sampleDto;
    private Faculty sampleEntity;

    @BeforeEach
    void setUp() {
        sampleDto = new FacultyDto(1, "Engineering");
        sampleEntity = new Faculty();
        sampleEntity.setId(1);
        sampleEntity.setFacultyName("Engineering");
    }

    // ---------- createFaculty ----------

    @Test
    void createFaculty_ShouldThrow_WhenDtoIsNull() {
        Exception ex = assertThrows(Exception.class, () -> facultyService.createFaculty(null));
        assertEquals("FacultyDto is null.", ex.getMessage());
        verifyNoInteractions(facultyMapper, facultyRepository);
    }

    @Test
    void createFaculty_ShouldPersistAndReturn_WhenValid() throws Exception {
        when(facultyMapper.toEntity(sampleDto)).thenReturn(sampleEntity);

        Faculty result = facultyService.createFaculty(sampleDto);

        assertEquals(sampleEntity, result);
        verify(facultyRepository).persist(sampleEntity);
    }

    // ---------- deleteFaculty ----------

    @Test
    void deleteFaculty_ShouldThrow_WhenIdIsZeroOrNegative() {
        Exception ex1 = assertThrows(Exception.class, () -> facultyService.deleteFaculty(0));
        assertEquals("Faculty ID is invalid.", ex1.getMessage());

        Exception ex2 = assertThrows(Exception.class, () -> facultyService.deleteFaculty(-5));
        assertEquals("Faculty ID is invalid.", ex2.getMessage());

        verifyNoInteractions(facultyRepository);
    }

    @Test
    void deleteFaculty_ShouldThrow_WhenEntityNotFound() {
        when(facultyRepository.deleteById(99L)).thenReturn(false);

        Exception ex = assertThrows(Exception.class, () -> facultyService.deleteFaculty(99L));
        assertEquals("Faculty with ID 99 does not exist.", ex.getMessage());
    }

    @Test
    void deleteFaculty_ShouldDelete_WhenEntityExists() throws Exception {
        when(facultyRepository.deleteById(1L)).thenReturn(true);

        facultyService.deleteFaculty(1L);

        verify(facultyRepository).deleteById(1L);
    }

    // ---------- getAllFaculties ----------

    @Test
    void getAllFaculties_ShouldReturnEmptyList_WhenNoFacultiesExist() throws Exception {
        when(facultyRepository.findAll().list()).thenReturn(Collections.emptyList());

        List<FacultyDto> result = facultyService.getAllFaculties();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllFaculties_ShouldReturnDtoList_WhenFacultiesExist() throws Exception {
        Faculty f1 = new Faculty();
        f1.setId(1);
        f1.setFacultyName("Engineering");

        Faculty f2 = new Faculty();
        f2.setId(2);
        f2.setFacultyName("Arts");

        when(facultyRepository.findAll().list()).thenReturn(Arrays.asList(f1, f2));

        List<FacultyDto> result = facultyService.getAllFaculties();

        assertEquals(2, result.size());
        assertEquals("Engineering", result.get(0).getFacultyName());
        assertEquals("Arts", result.get(1).getFacultyName());
    }

    // ---------- getFacultyById ----------

    @Test
    void getFacultyById_ShouldThrow_WhenIdIsZeroOrNegative() {
        Exception ex1 = assertThrows(Exception.class, () -> facultyService.getFacultyById(0));
        assertEquals("Faculty ID is invalid.", ex1.getMessage());

        Exception ex2 = assertThrows(Exception.class, () -> facultyService.getFacultyById(-10));
        assertEquals("Faculty ID is invalid.", ex2.getMessage());
    }

    @Test
    void getFacultyById_ShouldReturnFaculty_WhenValidId() throws Exception {
        when(facultyRepository.findById(1L)).thenReturn(sampleEntity);

        Faculty result = facultyService.getFacultyById(1L);

        assertEquals(sampleEntity, result);
    }

    // ---------- getFacultyByName ----------

    @Test
    void getFacultyByName_ShouldThrow_WhenNameIsNullOrEmpty() {
        Exception ex1 = assertThrows(Exception.class, () -> facultyService.getFacultyByName(null));
        assertEquals("Faculty name is null or empty.", ex1.getMessage());

        Exception ex2 = assertThrows(Exception.class, () -> facultyService.getFacultyByName(""));
        assertEquals("Faculty name is null or empty.", ex2.getMessage());
    }

    @Test
    void getFacultyByName_ShouldThrow_WhenNotFound() {
        when(facultyRepository.findByNameOptional("Law")).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> facultyService.getFacultyByName("Law"));
        assertEquals("Faculty with name 'Law' not found.", ex.getMessage());
    }

    @Test
    void getFacultyByName_ShouldReturnFaculty_WhenFound() throws Exception {
        when(facultyRepository.findByNameOptional("Engineering")).thenReturn(Optional.of(sampleEntity));

        Faculty result = facultyService.getFacultyByName("Engineering");

        assertEquals(sampleEntity, result);
    }

    // ---------- updateFaculty ----------

    @Test
    void updateFaculty_ShouldThrow_WhenDtoIsNull() {
        Exception ex = assertThrows(Exception.class, () -> facultyService.updateFaculty(null));
        assertEquals("FacultyDto is null.", ex.getMessage());
    }

    @Test
    void updateFaculty_ShouldThrow_WhenFacultyNotFound() {
        when(facultyRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> facultyService.updateFaculty(sampleDto));
        assertEquals("No faculty with such ID was found.", ex.getMessage());
    }

    @Test
    void updateFaculty_ShouldUpdateAndPersist_WhenFacultyFound() throws Exception {
        Faculty existing = new Faculty();
        existing.setId(1);
        existing.setFacultyName("Old Name");

        when(facultyRepository.findByIdOptional(1L)).thenReturn(Optional.of(existing));

        Faculty updated = facultyService.updateFaculty(sampleDto);

        assertEquals("Engineering", updated.getFacultyName());
        verify(facultyRepository).persist(existing);
    }
}

