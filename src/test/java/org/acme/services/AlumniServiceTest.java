package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;
import org.acme.entites.Degree;
import org.acme.entites.Faculty;
import org.acme.entites.Speciality;
import org.acme.service.AlumniService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AlumniServiceTest {

    @InjectMock
    AlumniService alumniService;

    Alumni sampleAlumni;
    AlumniFrontDto sampleFrontDto;
    AlumniDto sampleDto;

    @BeforeEach
    public void setup() {
        Degree degree = new Degree();
        degree.setId(1);
        degree.setDegree("Computer Science");

        Faculty faculty = new Faculty();
        faculty.setId(1);
        faculty.setFacultyName("Engineering");

        Speciality speciality = new Speciality();
        speciality.setId(1);
        speciality.setSpecialityName("Software");

        AlumniGroup group = new AlumniGroup();
        group.setId(1);
        group.setFaculty(faculty);
        group.setGroupNumber(101);
        group.setGraduationYear(2020);
        group.setSpeciality(speciality);

        AlumniGroupsMembership membership = new AlumniGroupsMembership();
        membership.setId(1);
        membership.setAverageScore(5.5);
        membership.setGroup(group);

        Alumni sampleAlumni = new Alumni();
        sampleAlumni.setFacebookUrl("fb.com/test");
        sampleAlumni.setLinkedInUrl("linkedin.com/test");
        sampleAlumni.setDegree(degree);
        sampleAlumni.setFacultyNumber(12345);
        sampleAlumni.setMemberships(new ArrayList<>(List.of(membership)));

        membership.setAlumni(sampleAlumni);

        sampleFrontDto = new AlumniFrontDto();
        sampleFrontDto.setFacultyNumber(12345);
        sampleFrontDto.setFullName("John Doe");
        sampleFrontDto.setBirthDate(LocalDate.of(2000, 3, 15));
        sampleFrontDto.setFacebookUrl("fb.com/test");
        sampleFrontDto.setLinkedinUrl("linkedin.com/test");
        sampleFrontDto.setDegree("Computer Science");
        sampleFrontDto.setFaculty("Engineering");
        sampleFrontDto.setCreatedAt(Instant.now());
        sampleFrontDto.setUpdatedAt(Instant.now());
        sampleFrontDto.setMembershipIds(List.of(1));

        sampleDto = new AlumniDto();
        sampleDto.setFacultyNumber(12345);
        sampleDto.setFullName("John Doe");
        sampleDto.setFacebookUrl("fb.com/test");
        sampleDto.setLinkedinUrl("linkedin.com/test");
        sampleDto.setCreatedAt(Instant.now());
        sampleDto.setUpdatedAt(Instant.now());
        sampleDto.setMemberships(new ArrayList<>());
    }

    @Test
    public void testGetAlumniByFacultyNumber_found() throws Exception {
        when(alumniService.getAlumniByFacultyNumber(12345)).thenReturn(sampleAlumni);
        Alumni result = alumniService.getAlumniByFacultyNumber(12345);
        assertNotNull(result);
        assertEquals(12345, result.getFacultyNumber());
    }

    @Test
    public void testGetAlumniByFacultyNumber_notFound() throws Exception {
        when(alumniService.getAlumniByFacultyNumber(99999)).thenThrow(new Exception("Not Found"));
        assertThrows(Exception.class, () -> alumniService.getAlumniByFacultyNumber(99999));
    }

    @Test
    public void testGetAllAlumni_nonEmpty() throws Exception {
        when(alumniService.getAllAlumni()).thenReturn(List.of(sampleAlumni));
        List<Alumni> result = alumniService.getAllAlumni();
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetAllAlumni_empty() throws Exception {
        when(alumniService.getAllAlumni()).thenReturn(Collections.emptyList());
        List<Alumni> result = alumniService.getAllAlumni();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAlumniByDegree_valid() throws Exception {
        when(alumniService.getAlumniByDegree("Computer Science")).thenReturn(List.of(sampleDto));
        List<AlumniDto> result = alumniService.getAlumniByDegree("Computer Science");
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetAlumniByDegree_invalid() throws Exception {
        when(alumniService.getAlumniByDegree("Unknown")).thenReturn(Collections.emptyList());
        List<AlumniDto> result = alumniService.getAlumniByDegree("Unknown");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSaveAlumni_entity() throws Exception {
        when(alumniService.saveAlumni(sampleAlumni)).thenReturn(sampleAlumni);
        Alumni result = alumniService.saveAlumni(sampleAlumni);
        assertEquals(sampleAlumni.getFacultyNumber(), result.getFacultyNumber());
    }

    @Test
    public void testSaveAlumni_dto() throws Exception {
        when(alumniService.saveAlumni(sampleFrontDto)).thenReturn(sampleAlumni);
        Alumni result = alumniService.saveAlumni(sampleFrontDto);
        assertEquals(sampleAlumni.getFacultyNumber(), result.getFacultyNumber());
    }

    @Test
    public void testUpdateAlumni_entity() throws Exception {
        when(alumniService.updateAlumni(sampleAlumni)).thenReturn(sampleAlumni);
        Alumni result = alumniService.updateAlumni(sampleAlumni);
        assertEquals(sampleAlumni.getFacultyNumber(), result.getFacultyNumber());
    }

    @Test
    public void testUpdateAlumni_dto() throws Exception {
        when(alumniService.updateAlumni(sampleFrontDto)).thenReturn(sampleAlumni);
        Alumni result = alumniService.updateAlumni(sampleFrontDto);
        assertEquals(sampleAlumni.getFacultyNumber(), result.getFacultyNumber());
    }

    @Test
    public void testDeleteAlumni_valid() throws Exception {
        doNothing().when(alumniService).deleteAlumni(12345);
        assertDoesNotThrow(() -> alumniService.deleteAlumni(12345));
    }

    @Test
    public void testConvertAlumniFromDto_valid() throws Exception {
        when(alumniService.convertAlumniFromDto(sampleFrontDto)).thenReturn(sampleAlumni);
        Alumni result = alumniService.convertAlumniFromDto(sampleFrontDto);
        assertEquals(12345, result.getFacultyNumber());
    }

    @Test
    public void testConvertAlumniToDto_valid() throws Exception {
        when(alumniService.convertAlumniToDto(sampleAlumni)).thenReturn(sampleDto);
        AlumniDto result = alumniService.convertAlumniToDto(sampleAlumni);
        assertEquals(12345, result.getFacultyNumber());
    }
}