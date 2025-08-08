package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.acme.avro.ambiguous.SpecialityDto;
import org.acme.entites.Speciality;
import org.acme.repository.SpecialityRepository;
import org.acme.service.implementations.SpecialityServiceImpl;
import org.acme.util.mappers.SpecialityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SpecialityServiceTest {

    @Mock
    private SpecialityRepository specialityRepository;

    @Mock
    private SpecialityMapper specialityMapper;

    @InjectMocks
    private SpecialityServiceImpl specialityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------- createSpeciality -------------

    @Test
    void createSpeciality_nullDto_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> specialityService.createSpeciality(null));
        assertEquals("SpecialityDto is null.", ex.getMessage());
    }

    @Test
    void createSpeciality_validDto_persistsAndReturnsEntity() throws Exception {
        SpecialityDto dto = new SpecialityDto(1, "Cardiology");
        Speciality entity = new Speciality(1, "Cardiology");

        when(specialityMapper.toEntity(dto)).thenReturn(entity);

        Speciality result = specialityService.createSpeciality(dto);

        verify(specialityRepository).persist(entity);
        assertEquals("Cardiology", result.getSpecialityName());
    }

    // ----------- deleteSpeciality -------------

    @Test
    void deleteSpeciality_invalidId_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> specialityService.deleteSpeciality(0));
        assertEquals("Speciality ID is invalid.", ex.getMessage());
    }

    @Test
    void deleteSpeciality_idNotFound_throwsException() {
        when(specialityRepository.deleteById(10L)).thenReturn(false);

        Exception ex = assertThrows(Exception.class, () -> specialityService.deleteSpeciality(10L));
        assertEquals("Speciality with ID 10 does not exist.", ex.getMessage());
    }

    @Test
    void deleteSpeciality_validId_deletesSuccessfully() throws Exception {
        when(specialityRepository.deleteById(5L)).thenReturn(true);

        specialityService.deleteSpeciality(5L);

        verify(specialityRepository).deleteById(5L);
    }

    // ----------- getAllSpecialities -------------

    @Test
    void getAllSpecialities_emptyList_returnsEmptyDtoList() throws Exception {
        when(specialityRepository.findAll().list()).thenReturn(Collections.emptyList());

        List<SpecialityDto> result = specialityService.getAllSpecialities();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllSpecialities_nonEmptyList_returnsDtoList() throws Exception {
        Speciality s1 = new Speciality(1, "Cardiology");
        Speciality s2 = new Speciality(2, "Neurology");
        when(specialityRepository.findAll().list()).thenReturn(List.of(s1, s2));

        List<SpecialityDto> result = specialityService.getAllSpecialities();

        assertEquals(2, result.size());
        assertEquals("Cardiology", result.get(0).getSpecialityName());
    }

    // ----------- getSpecialityById -------------

    @Test
    void getSpecialityById_invalidId_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> specialityService.getSpecialityById(0));
        assertEquals("Speciality ID is invalid.", ex.getMessage());
    }

    @Test
    void getSpecialityById_validId_returnsEntity() throws Exception {
        Speciality speciality = new Speciality(1, "Dermatology");
        when(specialityRepository.findById(1L)).thenReturn(speciality);

        Speciality result = specialityService.getSpecialityById(1L);

        assertEquals("Dermatology", result.getSpecialityName());
    }

    // ----------- getSpecialityByName -------------

    @Test
    void getSpecialityByName_nullName_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> specialityService.getSpecialityByName(null));
        assertEquals("Speciality name is null or empty.", ex.getMessage());
    }

    @Test
    void getSpecialityByName_emptyName_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> specialityService.getSpecialityByName(""));
        assertEquals("Speciality name is null or empty.", ex.getMessage());
    }

    @Test
    void getSpecialityByName_nameNotFound_throwsException() {
        when(specialityRepository.findByNameOptional("Pediatrics")).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> specialityService.getSpecialityByName("Pediatrics"));
        assertEquals("Speciality with name 'Pediatrics' not found.", ex.getMessage());
    }

    @Test
    void getSpecialityByName_validName_returnsEntity() throws Exception {
        Speciality speciality = new Speciality(2, "Oncology");
        when(specialityRepository.findByNameOptional("Oncology")).thenReturn(Optional.of(speciality));

        Speciality result = specialityService.getSpecialityByName("Oncology");

        assertEquals("Oncology", result.getSpecialityName());
    }

    // ----------- updateSpeciality -------------

    @Test
    void updateSpeciality_nullDto_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> specialityService.updateSpeciality(null));
        assertEquals("SpecialityDto is null.", ex.getMessage());
    }

    @Test
    void updateSpeciality_idNotFound_throwsException() {
        SpecialityDto dto = new SpecialityDto(1, "UpdatedName");
        when(specialityRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> specialityService.updateSpeciality(dto));
        assertEquals("No speciality with such ID was found.", ex.getMessage());
    }

    @Test
    void updateSpeciality_validDto_updatesAndReturnsEntity() throws Exception {
        SpecialityDto dto = new SpecialityDto(1, "UpdatedName");
        Speciality existing = new Speciality(1, "OldName");
        when(specialityRepository.findByIdOptional(1L)).thenReturn(Optional.of(existing));

        Speciality result = specialityService.updateSpeciality(dto);

        verify(specialityRepository).persist(existing);
        assertEquals("UpdatedName", result.getSpecialityName());
    }

    // ----------- convertToDtoList -------------

    @Test
    void convertToDtoList_empty_returnsEmptyList() {
        List<SpecialityDto> result = specialityService.convertToDtoList(Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    void convertToDtoList_nonEmpty_returnsMappedList() {
        Speciality s = new Speciality(1, "Neurosurgery");
        List<SpecialityDto> result = specialityService.convertToDtoList(List.of(s));
        assertEquals(1, result.size());
        assertEquals("Neurosurgery", result.get(0).getSpecialityName());
    }
}

