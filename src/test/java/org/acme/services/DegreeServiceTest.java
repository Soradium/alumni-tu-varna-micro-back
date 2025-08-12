package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.acme.avro.ambiguous.DegreeDto;
import org.acme.entites.Degree;
import org.acme.repository.DegreeRepository;
import org.acme.service.implementations.DegreeServiceImpl;
import org.acme.util.mappers.DegreeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DegreeServiceTest {

    @Mock
    private DegreeRepository degreeRepository;

    @Mock
    private DegreeMapper degreeMapper;

    @InjectMocks
    private DegreeServiceImpl degreeService;

    private Degree degree;
    private DegreeDto degreeDto;

    @BeforeEach
    void setUp() {
        degree = new Degree();
        degree.setId(1);
        degree.setDegree("Computer Science");

        degreeDto = new DegreeDto(1, "Computer Science");
    }

    // --- createDegree ---
    @Test
    void createDegree_validInput_persistsAndReturnsEntity() throws Exception {
        when(degreeMapper.toEntity(degreeDto)).thenReturn(degree);

        Degree result = degreeService.createDegree(degreeDto);

        verify(degreeRepository).persist(degree);
        assertEquals(degree, result);
    }

    // --- deleteDegree ---
    @Test
    void deleteDegree_invalidId_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> degreeService.deleteDegree(0));
        assertEquals("Degree ID entered is incorrect.", ex.getMessage());
        verify(degreeRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteDegree_validId_callsRepositoryDelete() throws Exception {
        degreeService.deleteDegree(1L);
        verify(degreeRepository).deleteById(1L);
    }

    // --- getAllDegrees ---
    @Test
    void getAllDegrees_emptyList_returnsEmptyList() throws Exception {
        when(degreeRepository.findAll().list()).thenReturn(Collections.emptyList());

        List<DegreeDto> result = degreeService.getAllDegrees();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllDegrees_nonEmptyList_returnsMappedList() throws Exception {
        when(degreeRepository.findAll().list()).thenReturn(Arrays.asList(degree));

        List<DegreeDto> result = degreeService.getAllDegrees();

        assertEquals(1, result.size());
        assertEquals("Computer Science", result.get(0).getDegreeName());
    }

    // --- getDegreeById ---
    @Test
    void getDegreeById_invalidId_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> degreeService.getDegreeById(0));
        assertEquals("Degree ID entered is incorrect.", ex.getMessage());
        verify(degreeRepository, never()).findById(anyLong());
    }

    @Test
    void getDegreeById_validId_returnsDegree() throws Exception {
        when(degreeRepository.findById(1L)).thenReturn(degree);

        Degree result = degreeService.getDegreeById(1L);

        assertEquals(degree, result);
    }

    // --- getDegreeByName ---
    @Test
    void getDegreeByName_nullName_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> degreeService.getDegreeByName(null));
        assertEquals("Name is null.", ex.getMessage());
    }

    @Test
    void getDegreeByName_notFound_throwsException() {
        when(degreeRepository.findByNameOptional("Math")).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> degreeService.getDegreeByName("Math"));
        assertEquals("No such degree name is found in the system.", ex.getMessage());
    }

    @Test
    void getDegreeByName_found_returnsDegree() throws Exception {
        when(degreeRepository.findByNameOptional("Computer Science")).thenReturn(Optional.of(degree));

        Degree result = degreeService.getDegreeByName("Computer Science");

        assertEquals(degree, result);
    }

    // --- updateDegree ---
    @Test
    void updateDegree_nullDto_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> degreeService.updateDegree(null));
        assertEquals("degreeDto is null.", ex.getMessage());
    }

    @Test
    void updateDegree_notFound_throwsException() {
        when(degreeRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> degreeService.updateDegree(degreeDto));
        assertEquals("No degree with such ID was found.", ex.getMessage());
    }

    @Test
    void updateDegree_found_updatesAndPersists() throws Exception {
        when(degreeRepository.findByIdOptional(1L)).thenReturn(Optional.of(degree));

        Degree result = degreeService.updateDegree(degreeDto);

        assertEquals("Computer Science", result.getDegreeName());
        verify(degreeRepository).persist(degree);
    }

    // --- convertToDtoList ---
    @Test
    void convertToDtoList_emptyList_returnsEmptyList() throws Exception {
        List<DegreeDto> result = degreeService.convertToDtoList(Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    void convertToDtoList_nonEmptyList_returnsDtoList() throws Exception {
        List<DegreeDto> result = degreeService.convertToDtoList(Arrays.asList(degree));
        assertEquals(1, result.size());
        assertEquals("Computer Science", result.get(0).getDegreeName());
    }
}
