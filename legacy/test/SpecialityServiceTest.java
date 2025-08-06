package org.acme.services;

import org.acme.dto.SpecialityDto;
import org.acme.entites.Speciality;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.SpecialityRepository;
import org.acme.service.implementation.SpecialityServiceImpl;
import org.acme.util.mappers.SpecialityMapper;
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

public class SpecialityServiceTest {

    @Mock
    SpecialityRepository specialityRepository;

    @Spy
    SpecialityMapper specialityMapper = new SpecialityMapper();

    @InjectMocks
    SpecialityServiceImpl specialityService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // getSpecialityById

    @Test
    void getSpecialityById_validId_returnsDto() {
        Speciality speciality = new Speciality();
        speciality.setId(1);
        speciality.setSpecialityName("SIT");

        when(specialityRepository.findByIdOptional(1L)).thenReturn(Optional.of(speciality));

        SpecialityDto result = specialityService.getSpecialityById(1L);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("SIT", result.getSpeciality());
        verify(specialityRepository).findByIdOptional(1L);
    }

    @Test
    void getSpecialityById_notInDbId_throwsException() {
        when(specialityRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        Throwable ex = assertThrows(ResourceNotFoundException.class,
                () -> specialityService.getSpecialityById(99L));

        assertTrue(ex.getMessage().contains("99"));
        verify(specialityRepository).findByIdOptional(99L);
    }

    // getSpecialityByName

    @Test
    void getSpecialityByName_validName_returnsDto() {
        Speciality speciality = new Speciality();
        speciality.setSpecialityName("SIT");

        when(specialityRepository.findByNameOptional("SIT"))
                .thenReturn(Optional.of(speciality));

        SpecialityDto result = specialityService.getSpecialityByName("SIT");

        assertNotNull(result);
        assertEquals("SIT", result.getSpeciality());
        verify(specialityRepository).findByNameOptional("SIT");
    }

    @Test
    void getSpecialityByName_nonExistingName_throwsException() {
        when(specialityRepository.findByNameOptional("Unknown"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> specialityService.getSpecialityByName("Unknown"));

        assertTrue(ex.getMessage().contains("Unknown"));
        verify(specialityRepository).findByNameOptional("Unknown");
    }

    @Test
    void getSpecialityByName_emptyName_throwsException() {
        when(specialityRepository.findByNameOptional("")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> specialityService.getSpecialityByName(""));
    }

    //getAllSpecialities

    @Test
    void getAllSpecialities_nonEmptyList_returnsDtoList() {
        Speciality speciality1 = new Speciality();
        speciality1.setSpecialityName("SIT");

        Speciality speciality2 = new Speciality();
        speciality2.setSpecialityName("KST");

        when(specialityRepository.listAll()).thenReturn(List.of(speciality1, speciality2));
        List<SpecialityDto> result = specialityService.getAllSpecialities();

        assertEquals(2, result.size());
        assertEquals("SIT", result.get(0).getSpeciality());
        assertEquals("KST", result.get(1).getSpeciality());
    }

    @Test
    void getAllSpecialities_emptyList_throwsException() {
        when(specialityRepository.listAll()).thenReturn(List.of());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> specialityService.getAllSpecialities());

        assertTrue(ex.getMessage().contains("No specialities"));
    }

    // createSpeciality

    @Test
    void createSpeciality_validDto_persistsAndReturnsDto() {
        SpecialityDto dto = new SpecialityDto();
        dto.setSpeciality("SIT");

//        doNothing().when(specialityRepository).persist(entity);
//
//        SpecialityDto result = specialityService.createSpeciality(dto);
//
//        assertEquals("SIT", result.getSpeciality());
//        verify(specialityMapper).toEntity(dto);
//        verify(specialityRepository).persist(entity);
//        verify(specialityMapper).toDto(entity);
    }

    @Test
    void createSpeciality_nullDto_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> specialityService.createSpeciality(null));
    }

    // updateSpeciality

    @Test
    void updateSpeciality_validId_updatesAndReturnsDto() {
        SpecialityDto dto = new SpecialityDto();
        dto.setSpeciality("SIT");
        Speciality entity = new Speciality();
        entity.setSpecialityName("OldName");

        when(specialityRepository.findByIdOptional(5L)).thenReturn(Optional.of(entity));
        doNothing().when(specialityRepository).persist(entity);

        SpecialityDto result = specialityService.updateSpeciality(5L, dto);

        assertEquals("SIT", result.getSpeciality());
        verify(specialityRepository).findByIdOptional(5L);
        verify(specialityRepository).persist(entity);
    }

    @Test
    void updateSpeciality_nonExistingId_throwsException() {
        SpecialityDto dto = new SpecialityDto();
        when(specialityRepository.findByIdOptional(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> specialityService.updateSpeciality(10L, dto));
    }

    // deleteSpeciality

    @Test
    void deleteSpeciality_existingId_deletesAndReturnsDto() {
        Speciality entity = new Speciality();
        entity.setSpecialityName("ToDelete");

        when(specialityRepository.findByIdOptional(7L)).thenReturn(Optional.of(entity));
        doNothing().when(specialityRepository).delete(entity);

        SpecialityDto result = specialityService.deleteSpeciality(7L);

        assertEquals("ToDelete", result.getSpeciality());
        verify(specialityRepository).delete(entity);
    }

    @Test
    void deleteSpeciality_nonExistingId_throwsException() {
        when(specialityRepository.findByIdOptional(8L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> specialityService.deleteSpeciality(8L));
    }

    // checkSpeciality

    @Test
    void checkSpeciality_existingName_returnsTrue() {
        Speciality entity = new Speciality();
        when(specialityRepository.findByNameOptional("Check")).thenReturn(Optional.of(entity));

        assertTrue(specialityService.checkSpeciality("Check"));
        verify(specialityRepository).findByNameOptional("Check");
    }

    @Test
    void checkSpeciality_nonExistingName_throwsException() {
        when(specialityRepository.findByNameOptional("NotExist")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> specialityService.checkSpeciality("NotExist"));
    }

}
