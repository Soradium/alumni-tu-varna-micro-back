package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import org.mockito.MockitoAnnotations;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class SpecialityServiceTest {

    @InjectMock
    private SpecialityRepository specialityRepository;

    @Inject
    private SpecialityMapper specialityMapper;
    @Inject
    private SpecialityServiceImpl specialityService;

    private Speciality sE1;
    private Speciality sE2;
    private SpecialityDto s1;
    private SpecialityDto s2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        s1 = new SpecialityDto(1, "Cardiology");
        sE1 = new Speciality(1, "Cardiology");
        s2 = new SpecialityDto(2, "Neurology");
        sE2 = new Speciality(2,  "Neurology");
    }

    // ----------- createSpeciality -------------

    @Test
    void createSpeciality_nullDto_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> specialityService.createSpeciality(null));
        assertEquals("SpecialityDto is null.", ex.getMessage());
    }

    @Test
    void createSpeciality_validDto_persistsAndReturnsEntity() throws Exception {

        Speciality result = specialityService.createSpeciality(s1);

        verify(specialityRepository).persist(any(Speciality.class));
        assertSame(s1.getSpecialityName(), result.getSpecialityName());
    }

    // ----------- deleteSpeciality -------------

    @Test
    void deleteSpeciality_invalidId_throwsException() {
        PanacheMock.mock(SpecialityServiceImpl.class);
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

        when(specialityRepository.listAll()).thenReturn(new ArrayList<>());

        List<SpecialityDto> result = specialityService.getAllSpecialities();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllSpecialities_nonEmptyList_returnsDtoList() throws Exception {

        when(specialityRepository.listAll()).thenReturn(List.of(sE1,sE2));

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

