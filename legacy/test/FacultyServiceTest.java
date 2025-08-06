package org.acme.services;

import org.acme.dto.FacultyDto;
import org.acme.entites.Faculty;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.FacultyRepository;
import org.acme.service.implementation.FacultyServiceImpl;
import org.acme.util.mappers.FacultyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;
import java.util.Optional;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class FacultyServiceTest {

    @Mock
    FacultyRepository facultyRepository;

    @Spy
    FacultyMapper facultyMapper = new FacultyMapper();

    @InjectMocks
    FacultyServiceImpl facultyService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // getFacultyById

    @Test
    void getFacultyById_validId_returnsDto() {
        Faculty faculty = new Faculty();
        faculty.setId(1);
        faculty.setFacultyName("faculty");

        when(facultyRepository.findByIdOptional(1L)).thenReturn(Optional.of(faculty));

        FacultyDto result = facultyService.getFacultyById(1L);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("faculty", result.getFacultyName());
        verify(facultyRepository).findByIdOptional(1L);
    }

    @Test
    void getFacultyById_notInDbId_throwsException() {
        when(facultyRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        Throwable ex = assertThrows(ResourceNotFoundException.class,
                () -> facultyService.getFacultyById(99L));

        assertTrue(ex.getMessage().contains("99"));
        verify(facultyRepository).findByIdOptional(99L);
    }

    // getFacultyByName

    @Test
    void getFacultyByName_validName_returnsDto() {
        Faculty faculty = new Faculty();
        faculty.setFacultyName("faculty");

        when(facultyRepository.findByNameOptional("faculty"))
                .thenReturn(Optional.of(faculty));

        FacultyDto result = facultyService.getFacultyByName("faculty");

        assertNotNull(result);
        assertEquals("faculty", result.getFacultyName());
        verify(facultyRepository).findByNameOptional("faculty");
    }

    @Test
    void getFacultyByName_nonExistingName_throwsException() {
        when(facultyRepository.findByNameOptional("Unknown"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> facultyService.getFacultyByName("Unknown"));

        assertTrue(ex.getMessage().contains("Unknown"));
        verify(facultyRepository).findByNameOptional("Unknown");
    }

    @Test
    void getFacultyByName_emptyName_throwsException() {
        when(facultyRepository.findByNameOptional("")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> facultyService.getFacultyByName(""));
    }

    //getAllFaculties

    @Test
    void getAllFaculties_nonEmptyList_returnsDtoList() {
        Faculty faculty1 = new Faculty();
        faculty1.setFacultyName("faculty1");

        Faculty faculty2 = new Faculty();
        faculty2.setFacultyName("faculty2");

        when(facultyRepository.listAll()).thenReturn(List.of(faculty1, faculty2));
        List<FacultyDto> result = facultyService.getAllFaculties();

        assertEquals(2, result.size());
        assertEquals("faculty1", result.get(0).getFacultyName());
        assertEquals("faculty2", result.get(1).getFacultyName());
    }

    @Test
    void getAllFaculties_emptyList_throwsException() {
        when(facultyRepository.listAll()).thenReturn(List.of());

        Throwable ex = assertThrows(ResourceNotFoundException.class,
                () -> facultyService.getAllFaculties());

        assertTrue(ex.getMessage().contains("No faculties"));
    }

    // createFaculty

    @Test
    void createFaculty_validDto_persistsAndReturnsDto() {
        FacultyDto dto = new FacultyDto();
        dto.setFacultyName("faculty");

//        doNothing().when(facultyRepository).persist(entity);
//
//        FacultyDto result = facultyService.createFaculty(dto);
    }

    @Test
    void createFaculty_nullDto_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> facultyService.createFaculty(null));
    }

    // updateFaculty

    @Test
    void updateFaculty_validId_updatesAndReturnsDto() {
        FacultyDto dto = new FacultyDto();
        dto.setFacultyName("faculty");
        Faculty entity = new Faculty();
        entity.setFacultyName("OldName");

        when(facultyRepository.findByIdOptional(5L)).thenReturn(Optional.of(entity));
        doNothing().when(facultyRepository).persist(entity);

        FacultyDto result = facultyService.updateFaculty(5L, dto);

        assertEquals("faculty", result.getFacultyName());
        verify(facultyRepository).findByIdOptional(5L);
        verify(facultyRepository).persist(entity);
    }

    @Test
    void updateFaculty_nonExistingId_throwsException() {
        FacultyDto dto = new FacultyDto();
        when(facultyRepository.findByIdOptional(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> facultyService.updateFaculty(10L, dto));
    }

    // deleteFaculty

    @Test
    void deleteFaculty_existingId_deletesAndReturnsDto() {
        Faculty entity = new Faculty();
        entity.setFacultyName("ToDelete");

        when(facultyRepository.findByIdOptional(7L)).thenReturn(Optional.of(entity));
        doNothing().when(facultyRepository).delete(entity);

        FacultyDto result = facultyService.deleteFaculty(7L);

        assertEquals("ToDelete", result.getFacultyName());
        verify(facultyRepository).delete(entity);
    }

    @Test
    void deleteFaculty_nonExistingId_throwsException() {
        when(facultyRepository.findByIdOptional(8L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> facultyService.deleteFaculty(8L));
    }

    // checkFaculty

    @Test
    void checkFaculty_existingName_returnsTrue() {
        Faculty entity = new Faculty();
        when(facultyRepository.findByNameOptional("Check")).thenReturn(Optional.of(entity));

        assertTrue(facultyService.isFacultyExist("Check"));
        verify(facultyRepository).findByNameOptional("Check");
    }

    @Test
    void checkFaculty_nonExistingName_throwsException() {
        when(facultyRepository.findByNameOptional("NotExist")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> facultyService.isFacultyExist("NotExist"));
    }

}
