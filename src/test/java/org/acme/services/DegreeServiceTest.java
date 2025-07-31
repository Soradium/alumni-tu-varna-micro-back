package org.acme.services;

import org.acme.dto.DegreeDto;
import org.acme.entites.Degree;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.DegreeRepository;
import org.acme.service.implementation.DegreeServiceImpl;
import org.acme.util.mappers.DegreeMapper;
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

public class DegreeServiceTest {

    @Mock
    DegreeRepository degreeRepository;

    @Spy
    DegreeMapper degreeMapper = new DegreeMapper();

    @InjectMocks
    DegreeServiceImpl degreeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // getDegreeById

    @Test
    void getDegreeById_validId_returnsDto() {
        Degree degree = new Degree();
        degree.setId(1);
        degree.setDegree("B");

        when(degreeRepository.findByIdOptional(1L)).thenReturn(Optional.of(degree));

        DegreeDto result = degreeService.getDegreeById(1L);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("B", result.getDegree());
        verify(degreeRepository).findByIdOptional(1L);
    }

    @Test
    void getDegreeById_notInDbId_throwsException() {
        when(degreeRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        Throwable ex = assertThrows(ResourceNotFoundException.class,
                () -> degreeService.getDegreeById(99L));

        assertTrue(ex.getMessage().contains("99"));
        verify(degreeRepository).findByIdOptional(99L);
    }

    // getDegreeByName

    @Test
    void getDegreeByName_validName_returnsDto() {
        Degree degree = new Degree();
        degree.setDegree("d");

        when(degreeRepository.findByNameOptional("d"))
                .thenReturn(Optional.of(degree));

        DegreeDto result = degreeService.getDegreeByName("d");

        assertNotNull(result);
        assertEquals("d", result.getDegree());
        verify(degreeRepository).findByNameOptional("d");
    }

    @Test
    void getDegreeByName_nonExistingName_throwsException() {
        when(degreeRepository.findByNameOptional("Unknown"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> degreeService.getDegreeByName("Unknown"));

        assertTrue(ex.getMessage().contains("Unknown"));
        verify(degreeRepository).findByNameOptional("Unknown");
    }

    @Test
    void getDegreeByName_emptyName_throwsException() {
        when(degreeRepository.findByNameOptional("")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> degreeService.getDegreeByName(""));
    }

    //getAllDegrees

    @Test
    void getAllDegrees_nonEmptyList_returnsDtoList() {
        Degree degree1 = new Degree();
        degree1.setDegree("d1");

        Degree degree2 = new Degree();
        degree2.setDegree("d2");

        when(degreeRepository.listAll()).thenReturn(List.of(degree1, degree2));
        List<DegreeDto> result = degreeService.getAllDegrees();

        assertEquals(2, result.size());
        assertEquals("d1", result.get(0).getDegree());
        assertEquals("d2", result.get(1).getDegree());
    }

    @Test
    void getAllDegrees_emptyList_throwsException() {
        when(degreeRepository.listAll()).thenReturn(List.of());

        Throwable ex = assertThrows(ResourceNotFoundException.class,
                () -> degreeService.getAllDegrees());

        assertTrue(ex.getMessage().contains("No degrees"));
    }

    // createDegree

    @Test
    void createDegree_validDto_persistsAndReturnsDto() {
        DegreeDto dto = new DegreeDto();
        dto.setDegree("d");

//        doNothing().when(facultyRepository).persist(entity);
//
//        FacultyDto result = facultyService.createFaculty(dto);
    }

    @Test
    void createDegree_nullDto_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> degreeService.createDegree(null));
    }

    // updateDegree

    @Test
    void updateDegree_validId_updatesAndReturnsDto() {
        DegreeDto dto = new DegreeDto();
        dto.setDegree("d");
        Degree entity = new Degree();
        entity.setDegree("OldName");

        when(degreeRepository.findByIdOptional(5L)).thenReturn(Optional.of(entity));
        doNothing().when(degreeRepository).persist(entity);

        DegreeDto result = degreeService.updateDegree(5L, dto);

        assertEquals("d", result.getDegree());
        verify(degreeRepository).findByIdOptional(5L);
        verify(degreeRepository).persist(entity);
    }

    @Test
    void updateDegree_nonExistingId_throwsException() {
        DegreeDto dto = new DegreeDto();
        when(degreeRepository.findByIdOptional(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> degreeService.updateDegree(10L, dto));
    }

    // deleteDegree

    @Test
    void deleteDegree_existingId_deletesAndReturnsDto() {
        Degree entity = new Degree();
        entity.setDegree("ToDelete");

        when(degreeRepository.findByIdOptional(7L)).thenReturn(Optional.of(entity));
        doNothing().when(degreeRepository).delete(entity);

        DegreeDto result = degreeService.deleteDegree(7L);

        assertEquals("ToDelete", result.getDegree());
        verify(degreeRepository).delete(entity);
    }

    @Test
    void deleteDegree_nonExistingId_throwsException() {
        when(degreeRepository.findByIdOptional(8L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> degreeService.deleteDegree(8L));
    }


}
