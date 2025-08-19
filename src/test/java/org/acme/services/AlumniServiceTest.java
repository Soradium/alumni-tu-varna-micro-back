package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.acme.avro.ambiguous.AlumniGroupDtoSimplified;
import org.acme.avro.ambiguous.DegreeDto;
import org.acme.avro.ambiguous.FacultyDto;
import org.acme.avro.ambiguous.SpecialityDto;
import org.acme.avro.back.AlumniDto;
import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.avro.front.AlumniGroupMembershipFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;
import org.acme.entites.Degree;
import org.acme.entites.Faculty;
import org.acme.entites.Speciality;
import org.acme.repository.AlumniDetailsRepository;
import org.acme.repository.AlumniRepository;
import org.acme.repository.DegreeRepository;
import org.acme.service.AlumniService;
import org.acme.util.mappers.AlumniMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class AlumniServiceTest {

    @InjectMock
    AlumniRepository alumniRepository;
    
    @InjectMock
    AlumniDetailsRepository alumniDetailsRepository;

    @InjectMock
    DegreeRepository degreeRepository;

    @Inject
    AlumniMapper mapper;

    @Inject
    AlumniService alumniService;

    Degree degree;
    Faculty faculty;
    Speciality speciality;
    AlumniGroup group;
    AlumniGroupsMembership membership;
    AlumniGroupMembershipDto membershipDto;
    Alumni sampleAlumni;
    AlumniDetails sampleDetails;
    AlumniFrontDto sampleFrontDto;
    AlumniDto sampleDto;
    AlumniGroupDtoSimplified alumniGroupDtoSimplified;
    AlumniGroupMembershipFrontDto alumniGroupMembershipFrontDto;

    @BeforeEach
    void setup() {
        // Entities
        degree = new Degree();
        degree.setId(1);
        degree.setDegree("Computer Science");

        faculty = new Faculty();
        faculty.setId(1);
        faculty.setFacultyName("Engineering");

        speciality = new Speciality();
        speciality.setId(1);
        speciality.setSpecialityName("Software");

        membership = new AlumniGroupsMembership();
        membership.setId(1);
        membership.setAverageScore(5.5);

        group = new AlumniGroup();
        group.setId(1);
        group.setFaculty(faculty);
        group.setGroupNumber(101);
        group.setGraduationYear(2020);
        group.setSpeciality(speciality);
        group.setMemberships(List.of(membership));

        membership.setGroup(group);

        sampleAlumni = new Alumni();
        sampleAlumni.setFacultyNumber(12345);
        sampleAlumni.setFacebookUrl("fb.com/test");
        sampleAlumni.setLinkedInUrl("linkedin.com/test");
        sampleAlumni.setDegree(degree);
        sampleAlumni.setMemberships(List.of(membership));
        membership.setAlumni(sampleAlumni);

        sampleDetails = new AlumniDetails();
        sampleDetails.setFacultyNumber(12345);
        sampleDetails.setFullName("John Doe");
        sampleDetails.setBirthDate(LocalDate.of(2000, 3, 15));
        sampleDetails.setFaculty(faculty);
        sampleDetails.setCreatedAt(Timestamp.from(Instant.now()));
        sampleDetails.setUpdatedAt(Timestamp.from(Instant.now()));

        // DTOs
        membershipDto = new AlumniGroupMembershipDto();
        membershipDto.setId(1);
        membershipDto.setAverageScore(5.5);
        membershipDto.setFacultyNumber(12345);

        AlumniGroupBackDto groupBackDto = new AlumniGroupBackDto();
        groupBackDto.setId(1);
        groupBackDto.setFaculty(new FacultyDto(1, "Engineering"));
        groupBackDto.setGroupNumber(101);
        groupBackDto.setGraduationYear(2020);
        groupBackDto.setSpeciality(new SpecialityDto(1, "Software"));
        groupBackDto.setMembershipIds(List.of(1));
        membershipDto.setGroup(groupBackDto);

        sampleDto = new AlumniDto();
        sampleDto.setFacultyNumber(12345);
        sampleDto.setFullName("John Doe");
        sampleDto.setFacebookUrl("fb.com/test");
        sampleDto.setLinkedinUrl("linkedin.com/test");
        sampleDto.setBirthDate(LocalDate.of(2000, 3, 15));
        sampleDto.setDegree(new DegreeDto(1, "Computer Science"));
        sampleDto.setFaculty(new FacultyDto(1, "Engineering"));
        sampleDto.setMemberships(List.of(membershipDto));
        sampleDto.setCreatedAt(Instant.now());
        sampleDto.setUpdatedAt(Instant.now());

        sampleFrontDto = new AlumniFrontDto();
        sampleFrontDto.setFacultyNumber(12345);
        sampleFrontDto.setFullName("John Doe");
        sampleFrontDto.setBirthDate(LocalDate.of(2000, 3, 15));
        sampleFrontDto.setFacebookUrl("fb.com/test");
        sampleFrontDto.setLinkedinUrl("linkedin.com/test");
        sampleFrontDto.setDegree("Computer Science");
        sampleFrontDto.setFaculty("Engineering");
        sampleFrontDto.setMembershipIds(List.of(1));
        sampleFrontDto.setCreatedAt(Instant.now());
        sampleFrontDto.setUpdatedAt(Instant.now());

        alumniGroupDtoSimplified = new AlumniGroupDtoSimplified();
        alumniGroupDtoSimplified.setId(1);
        alumniGroupDtoSimplified.setFaculty(new FacultyDto(1, "Engineering"));
        alumniGroupDtoSimplified.setGroupNumber(101);
        alumniGroupDtoSimplified.setGraduationYear(2020);
        alumniGroupDtoSimplified.setSpeciality(new SpecialityDto(1, "Software"));

        alumniGroupMembershipFrontDto = new AlumniGroupMembershipFrontDto();
        alumniGroupMembershipFrontDto.setId(1);
        alumniGroupMembershipFrontDto.setFacultyNumber(12345);
        alumniGroupMembershipFrontDto.setGroupNumber(101);
        alumniGroupMembershipFrontDto.setAverageScore(5.5);

    }


    @Test
    void testGetAlumniByFacultyNumber_found() throws Exception {
        when(alumniDetailsRepository.findByIdOptional(12345L)).thenReturn(Optional.of(sampleDetails));
        when(alumniRepository.findByIdOptional(12345L)).thenReturn(Optional.of(sampleAlumni));

        AlumniDto result = alumniService.getAlumniDtoByFacultyNumber(sampleFrontDto.getFacultyNumber());

        assertEquals(sampleFrontDto.getFacultyNumber(), result.getFacultyNumber());
    }

    @Test
    void testGetAlumniByFacultyNumber_notFound() {
        when(alumniRepository.findByIdOptional(99999L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> alumniService.getAlumniByFacultyNumber(99999));
    }

    @Test
    void testGetAllAlumni_nonEmpty() throws Exception {
        when(alumniRepository.listAll()).thenReturn(List.of(sampleAlumni));

        List<Alumni> result = alumniService.getAllAlumni();

        assertEquals(1, result.size());
        assertEquals(sampleAlumni.getFacultyNumber(), result.get(0).getFacultyNumber());
    }

    @Test
    void testGetAllAlumni_empty() throws Exception {
        when(alumniRepository.listAll()).thenReturn(Collections.emptyList());

        List<Alumni> result = alumniService.getAllAlumni();

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAlumniByDegree_valid() throws Exception {
        when(alumniRepository.findAllAlumniByDegree("Computer Science"))
        .thenReturn(List.of(sampleAlumni));

        when(alumniDetailsRepository.findAllAlumniDetailsForAllAlumni(List.of(sampleAlumni)))
            .thenReturn(List.of(sampleDetails));

        List<AlumniDto> result = alumniService.getAlumniByDegree("Computer Science");

        assertEquals(1, result.size());
        assertEquals(sampleDto.getFacultyNumber(), result.get(0).getFacultyNumber());
    }

    @Test
    void testGetAlumniByDegree_invalid() throws Exception {
        when(alumniRepository.findAllAlumniByDegree("Unknown")).thenReturn(Collections.emptyList());

        List<AlumniDto> result = alumniService.getAlumniByDegree("Unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveAlumni_entity() throws Exception {
        Alumni result = alumniService.saveAlumni(sampleAlumni, sampleDetails);

        verify(alumniRepository).persist(any(Alumni.class));
        verify(alumniDetailsRepository).persist(any(AlumniDetails.class));
        assertEquals(sampleAlumni.getFacultyNumber(), result.getFacultyNumber());
    }

    @Test
    void testSaveAlumni_dto() throws Exception {
        Alumni result = alumniService.saveAlumni(sampleFrontDto);

        verify(alumniRepository).persist(any(Alumni.class));
        verify(alumniDetailsRepository).persist(any(AlumniDetails.class));

        assertEquals(sampleFrontDto.getFacultyNumber(), result.getFacultyNumber());
    }

    @Test
    void testUpdateAlumni_entity() throws Exception {
        when(alumniRepository.findById(12345L)).thenReturn(sampleAlumni);

        Alumni updated = alumniService.updateAlumni(sampleAlumni);

        verify(alumniRepository).persist(any(Alumni.class));

        assertEquals(sampleAlumni.getFacultyNumber(), updated.getFacultyNumber());
    }

    @Test
    void testUpdateAlumni_dto() throws Exception {
        when(alumniRepository.findById(12345L)).thenReturn(sampleAlumni);
        when(degreeRepository.findByNameOptional("Computer Science"))
            .thenReturn(Optional.of(degree));
        Alumni result = alumniService.updateAlumni(sampleFrontDto);

        verify(alumniRepository).persist(any(Alumni.class));

        assertEquals(sampleFrontDto.getFacultyNumber(), result.getFacultyNumber());
    }

    @Test
    void testDeleteAlumni_valid() throws Exception {
        when(alumniRepository.findById(12345L)).thenReturn(sampleAlumni);

        alumniService.deleteAlumni(12345);

        verify(alumniRepository).delete(sampleAlumni);
    }
}
