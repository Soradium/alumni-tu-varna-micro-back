package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;
import org.acme.entites.Faculty;
import org.acme.repository.AlumniDetailsRepository;
import org.acme.repository.AlumniRepository;
import org.acme.repository.FacultyRepository;
import org.acme.service.implementations.AlumniDetailsServiceImpl;
import org.acme.util.mappers.AlumniMapper;
import org.acme.util.mappers.FacultyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class AlumniDetailsServiceTest {

    @InjectMock
    private AlumniDetailsRepository detailsRepository;

    @InjectMock
    private AlumniRepository alumniRepository;

    @InjectMock
    private FacultyRepository facultyRepository;

    @Inject
    private AlumniMapper alumniMapper;

    @Inject
    private FacultyMapper facultyMapper;

    @Inject
    private AlumniDetailsServiceImpl service;

    private AlumniDetails d;
    private Alumni a;
    private AlumniDto dto;
    private AlumniFrontDto dtoNew;
    private AlumniDetails dNew;
    private Faculty faculty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        faculty = new Faculty();
        faculty.setId(11);
        faculty.setFacultyName("Engineering");

        d = new AlumniDetails();
        d.setFacultyNumber(1);
        d.setBirthDate(LocalDate.of(2001, 1, 1));
        d.setFaculty(faculty);
        d.setFullName("John Doe");

        a = new Alumni();
        a.setFacultyNumber(1);

        dto = new AlumniDto();
        dto.setFacultyNumber(1);
        dto.setFaculty(facultyMapper.toDto(faculty));
        dto.setFullName("John Doe");

        dNew = new AlumniDetails();
        dNew.setFacultyNumber(1);
        dNew.setBirthDate(LocalDate.of(2000, 1, 1));

        dtoNew = new AlumniFrontDto();
        dtoNew.setFacultyNumber(7);
        dtoNew.setFaculty("Engineering");

        a.memberships = new ArrayList<>();
        d.setFullName("John Doe");
        d.setCreatedAt(Timestamp.valueOf(LocalDateTime.of(2000, 1, 1, 1, 1, 1)));
        d.setUpdatedAt(Timestamp.valueOf(LocalDateTime.of(2000, 1, 1, 1, 1, 1)));

    }

    // ----------- getAllAlumniDetails -------------

    @Test
    void getAllAlumniDetails_nonEmptyList_returnsList() throws Exception {
        when(detailsRepository.listAll()).thenReturn(List.of(d));

        List<AlumniDetails> result = service.getAllAlumniDetails();

        assertEquals(1, result.size());
    }

    @Test
    void getAllAlumniDetails_emptyList_returnsEmptyList() throws Exception {
        when(detailsRepository.listAll()).thenReturn(Collections.emptyList());

        List<AlumniDetails> result = service.getAllAlumniDetails();

        assertTrue(result.isEmpty());
    }

    // ----------- getAlumniListByFaculty -------------

    @Test
    void getAlumniListByFaculty_validFaculty_returnsDtoList() throws Exception {
        PanacheQuery<AlumniDetails> query = mock(PanacheQuery.class);

        when(detailsRepository.find("faculty.facultyName", "Engineering")).thenReturn(query);
        when(query.list()).thenReturn(List.of(d));

        when(alumniRepository.findByIdOptional(1L)).thenReturn(Optional.of(a));
        
        List<AlumniDto> result = service.getAlumniListByFaculty("Engineering");

        assertEquals(1, result.size());
        assertEquals(dto.getFaculty().getFacultyName(), result.get(0).getFaculty().getFacultyName());
    }

    @Test
    void getAlumniListByFaculty_idMismatch_throwsException() {
        PanacheQuery<AlumniDetails> query = mock(PanacheQuery.class);
        when(detailsRepository.find("faculty.facultyName", "Engineering")).thenReturn(query);
        when(query.list()).thenReturn(List.of(d));
        when(alumniRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getAlumniListByFaculty("Engineering"));
        assertTrue(ex.getMessage().contains("ID mismatch"));
    }

    // ----------- getAlumniListByFullName -------------

    @Test
    void getAlumniListByFullName_validName_returnsDtoList() throws Exception {
        PanacheQuery<AlumniDetails> query = mock(PanacheQuery.class);

        when(detailsRepository.find("fullName", "John Doe")).thenReturn(query);
        when(query.list()).thenReturn(List.of(d));
        when(alumniRepository.findByIdOptional(1L)).thenReturn(Optional.of(a));

        List<AlumniDto> result = service.getAlumniListByFullName("John Doe");

        assertEquals(1, result.size());
        assertEquals(dto.getFullName(), result.get(0).getFullName());
    }

    @Test
    void getAlumniListByFullName_idMismatch_throwsException() {
        PanacheQuery<AlumniDetails> query = mock(PanacheQuery.class);

        when(detailsRepository.find("fullName", "John Doe")).thenReturn(query);
        when(query.list()).thenReturn(List.of(d));
        when(alumniRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getAlumniListByFullName("John Doe"));
        assertTrue(ex.getMessage().contains("ID mismatch"));
    }

    // ----------- getAlumniDetailsByFacultyNumber -------------

    @Test
    void getAlumniDetailsByFacultyNumber_validId_returnsEntity() throws Exception {
        when(detailsRepository.findByIdOptional(1L)).thenReturn(Optional.of(d));

        AlumniDetails result = service.getAlumniDetailsByFacultyNumber(1);

        assertSame(d, result);
    }

    @Test
    void getAlumniDetailsByFacultyNumber_notFound_throwsException() {
        when(detailsRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class,
                () -> service.getAlumniDetailsByFacultyNumber(1));
        assertTrue(ex.getMessage().contains("No details"));
    }

    // ----------- getDetailsForAlumni -------------

    @Test
    void getDetailsForAlumni_validAlumni_returnsEntity() throws Exception {
        when(detailsRepository.findByIdOptional(1L)).thenReturn(Optional.of(d));

        AlumniDetails result = service.getDetailsForAlumni(a);

        assertSame(d, result);
    }

    @Test
    void getDetailsForAlumni_notFound_throwsException() {
        when(detailsRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class,
                () -> service.getDetailsForAlumni(a));
        assertTrue(ex.getMessage().contains("No details"));
    }

    // ----------- getDetailsForAlumniDto -------------

    @Test
    void getDetailsForAlumniDto_validDto_returnsEntity() throws Exception {
        when(detailsRepository.findByIdOptional(1L)).thenReturn(Optional.of(d));

        AlumniDetails result = service.getDetailsForAlumniDto(dto);

        assertSame(d, result);
    }

    @Test
    void getDetailsForAlumniDto_notFound_throwsException() {
        when(detailsRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class,
                () -> service.getDetailsForAlumniDto(dto));
        assertTrue(ex.getMessage().contains("No details"));
    }

    // ----------- getDetailsForListOfAlumni -------------

    @Test
    void getDetailsForListOfAlumni_validList_returnsDetailsList() throws Exception {
        when(detailsRepository.findByIdOptional(1L)).thenReturn(Optional.of(d));

        List<AlumniDetails> result = service.getDetailsForListOfAlumni(List.of(a));

        assertEquals(List.of(d), result);
    }

    @Test
    void getDetailsForListOfAlumni_notFound_throwsException() {
        when(detailsRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getDetailsForListOfAlumni(List.of(a)));
        assertTrue(ex.getMessage().contains("No details"));
    }

    // ----------- updateAlumniDetails (AlumniDetails) -------------

    @Test
    void updateAlumniDetails_entityExists_updatesAndReturnsEntity() throws Exception {
        when(detailsRepository.findByIdOptional(1L)).thenReturn(Optional.of(d));

        AlumniDetails result = service.updateAlumniDetails(dNew);
        
        verify(detailsRepository).persist(any(AlumniDetails.class));
        assertEquals(dNew.getFacultyNumber(), result.getFacultyNumber());
        assertEquals(dNew.getBirthDate(), result.getBirthDate());
    }

    @Test
    void updateAlumniDetails_entityNotFound_throwsException() {
        when(detailsRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class,
                () -> service.updateAlumniDetails(d));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    // ----------- updateAlumniDetails (AlumniFrontDto) -------------

    @Test
    void updateAlumniDetails_frontDtoExists_updatesAndReturnsEntity() throws Exception {
        when(detailsRepository.findByIdOptional(7L)).thenReturn(Optional.of(d));
        when(facultyRepository.findByName("Engineering")).thenReturn(faculty);

        AlumniDetails result = service.updateAlumniDetails(dtoNew);

        assertEquals("Engineering", result.getFaculty().getFacultyName());
        verify(detailsRepository).persist(result);
    }

    @Test
    void updateAlumniDetails_frontDtoNotFound_throwsException() {
        when(detailsRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class,
                () -> service.updateAlumniDetails(dtoNew));
        assertTrue(ex.getMessage().contains("does not exist"));
    }
}


