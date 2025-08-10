package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

public class AlumniDetailsServiceTest {

    @Mock
PanacheQuery<AlumniDetails> queryMock;


    @Mock
    private AlumniDetailsRepository detailsRepository;

    @Mock
    private AlumniRepository alumniRepository;

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private AlumniMapper alumniMapper;

    @InjectMocks
    private AlumniDetailsServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- getAllAlumniDetails ----------
    @Test
    void testGetAllAlumniDetails_returnsList() throws Exception {
        List<AlumniDetails> details = List.of(new AlumniDetails());
        when(detailsRepository.findAll().list()).thenReturn(details);

        List<AlumniDetails> result = service.getAllAlumniDetails();

        assertEquals(details, result);
        verify(detailsRepository).findAll();
    }

    @Test
    void testGetAllAlumniDetails_emptyList() throws Exception {
        when(detailsRepository.findAll().list()).thenReturn(Collections.emptyList());

        List<AlumniDetails> result = service.getAllAlumniDetails();

        assertTrue(result.isEmpty());
    }

    // ---------- getAlumniListByFaculty ----------
    @Test
    void testGetAlumniListByFaculty_success() throws Exception {
        AlumniDetails d = new AlumniDetails();
        d.setFacultyNumber(1);
        Alumni a = new Alumni();
        a.setFacultyNumber(1);
        AlumniDto dto = new AlumniDto();

        when(detailsRepository.find("faculty.facultyName", "Engineering").list()).thenReturn(List.of(d));
        when(alumniRepository.findByIdOptional(1L)).thenReturn(Optional.of(a));
        when(alumniMapper.toDto(a, d)).thenReturn(dto);

        List<AlumniDto> result = service.getAlumniListByFaculty("Engineering");

        assertEquals(1, result.size());
        assertSame(dto, result.get(0));
    }

    @Test
    void testGetAlumniListByFaculty_idMismatchThrows() {
        AlumniDetails d = new AlumniDetails();
        d.setFacultyNumber(1);

        when(detailsRepository.find("faculty.facultyName", "Engineering").list()).thenReturn(List.of(d));
        when(alumniRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getAlumniListByFaculty("Engineering"));
        assertTrue(ex.getMessage().contains("ID mismatch"));
    }

    // ---------- getAlumniListByFullName ----------
    @Test
    void testGetAlumniListByFullName_success() throws Exception {
        AlumniDetails d = new AlumniDetails();
        d.setFacultyNumber(2);
        Alumni a = new Alumni();
        a.setFacultyNumber(2);
        AlumniDto dto = new AlumniDto();

        when(detailsRepository.find("fullName", "John Doe").list()).thenReturn(List.of(d));
        when(alumniRepository.findByIdOptional(2L)).thenReturn(Optional.of(a));
        when(alumniMapper.toDto(a, d)).thenReturn(dto);

        List<AlumniDto> result = service.getAlumniListByFullName("John Doe");

        assertEquals(1, result.size());
        assertSame(dto, result.get(0));
    }

    @Test
    void testGetAlumniListByFullName_idMismatchThrows() {
        AlumniDetails d = new AlumniDetails();
        d.setFacultyNumber(2);

        when(detailsRepository.find("fullName", "John Doe").list()).thenReturn(List.of(d));
        when(alumniRepository.findByIdOptional(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getAlumniListByFullName("John Doe"));
        assertTrue(ex.getMessage().contains("ID mismatch"));
    }

    // ---------- getAlumniDetailsByFacultyNumber ----------
    @Test
    void testGetAlumniDetailsByFacultyNumber_success() throws Exception {
        AlumniDetails d = new AlumniDetails();
        when(detailsRepository.findByIdOptional(1L)).thenReturn(Optional.of(d));

        AlumniDetails result = service.getAlumniDetailsByFacultyNumber(1);

        assertSame(d, result);
    }

    @Test
    void testGetAlumniDetailsByFacultyNumber_notFoundThrows() {
        when(detailsRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class,
                () -> service.getAlumniDetailsByFacultyNumber(1));
        assertTrue(ex.getMessage().contains("No details"));
    }

    // ---------- getDetailsForAlumni ----------
    @Test
    void testGetDetailsForAlumni_success() throws Exception {
        Alumni a = new Alumni();
        a.setFacultyNumber(3);
        AlumniDetails d = new AlumniDetails();

        when(detailsRepository.findByIdOptional(3L)).thenReturn(Optional.of(d));

        AlumniDetails result = service.getDetailsForAlumni(a);
        assertSame(d, result);
    }

    @Test
    void testGetDetailsForAlumni_notFoundThrows() {
        Alumni a = new Alumni();
        a.setFacultyNumber(3);
        when(detailsRepository.findByIdOptional(3L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class,
                () -> service.getDetailsForAlumni(a));
        assertTrue(ex.getMessage().contains("No details"));
    }

    // ---------- getDetailsForAlumniDto ----------
    @Test
    void testGetDetailsForAlumniDto_success() throws Exception {
        AlumniDto a = new AlumniDto();
        a.setFacultyNumber(4);
        AlumniDetails d = new AlumniDetails();

        when(detailsRepository.findByIdOptional(4L)).thenReturn(Optional.of(d));

        AlumniDetails result = service.getDetailsForAlumniDto(a);
        assertSame(d, result);
    }

    @Test
    void testGetDetailsForAlumniDto_notFoundThrows() {
        AlumniDto a = new AlumniDto();
        a.setFacultyNumber(4);
        when(detailsRepository.findByIdOptional(4L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class,
                () -> service.getDetailsForAlumniDto(a));
        assertTrue(ex.getMessage().contains("No details"));
    }

    // ---------- getDetailsForListOfAlumni ----------
    @Test
    void testGetDetailsForListOfAlumni_success() throws Exception {
        Alumni a = new Alumni();
        a.setFacultyNumber(5);
        AlumniDetails d = new AlumniDetails();

        when(detailsRepository.findByIdOptional(5L)).thenReturn(Optional.of(d));

        List<AlumniDetails> result = service.getDetailsForListOfAlumni(List.of(a));
        assertEquals(List.of(d), result);
    }

    @Test
    void testGetDetailsForListOfAlumni_notFoundThrows() {
        Alumni a = new Alumni();
        a.setFacultyNumber(5);
        when(detailsRepository.findByIdOptional(5L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getDetailsForListOfAlumni(List.of(a)));
        assertTrue(ex.getMessage().contains("No details"));
    }

    // ---------- updateAlumniDetails (AlumniDetails) ----------
    @Test
    void testUpdateAlumniDetails_withEntity_success() throws Exception {
        AlumniDetails d = new AlumniDetails();
        d.setFacultyNumber(6);
        AlumniDetails existing = new AlumniDetails();
        existing.setFacultyNumber(6);

        when(detailsRepository.findByIdOptional(6L)).thenReturn(Optional.of(existing));

        AlumniDetails result = service.updateAlumniDetails(d);

        assertSame(existing, result);
        verify(detailsRepository).persist(existing);
        assertNotNull(existing.getUpdatedAt());
    }

    @Test
    void testUpdateAlumniDetails_withEntity_notFoundThrows() {
        AlumniDetails d = new AlumniDetails();
        d.setFacultyNumber(6);
        when(detailsRepository.findByIdOptional(6L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class,
                () -> service.updateAlumniDetails(d));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    // ---------- updateAlumniDetails (AlumniFrontDto) ----------
    @Test
    void testUpdateAlumniDetails_withFrontDto_success() throws Exception {
        AlumniFrontDto dto = new AlumniFrontDto();
        dto.setFacultyNumber(7);
        dto.setFaculty("Engineering");

        AlumniDetails existing = new AlumniDetails();
        existing.setFacultyNumber(7);

        Faculty faculty = new Faculty();

        when(detailsRepository.findByIdOptional(7L)).thenReturn(Optional.of(existing));
        when(facultyRepository.findByName("Engineering")).thenReturn(faculty);

        AlumniDetails result = service.updateAlumniDetails(dto);

        assertSame(existing, result);
        assertSame(faculty, existing.getFaculty());
        verify(detailsRepository).persist(existing);
        assertNotNull(existing.getUpdatedAt());
    }

    @Test
    void testUpdateAlumniDetails_withFrontDto_notFoundThrows() {
        AlumniFrontDto dto = new AlumniFrontDto();
        dto.setFacultyNumber(7);
        when(detailsRepository.findByIdOptional(7L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class,
                () -> service.updateAlumniDetails(dto));
        assertTrue(ex.getMessage().contains("does not exist"));
    }
}

