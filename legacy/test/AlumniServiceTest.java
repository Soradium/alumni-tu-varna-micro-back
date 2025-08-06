package org.acme.services;

import org.acme.avro.AlumniDto;
import org.acme.avro.AlumniGroupMembershipDto;
import org.acme.entites.*;
import org.acme.exceptions.IncorrectAlumnusNumberException;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.AlumniDetailsRepository;
import org.acme.repository.AlumniRepository;
import org.acme.service.AlumniGroupService;
import org.acme.service.DegreeService;
import org.acme.service.FacultyService;
import org.acme.service.implementation.AlumniServiceImpl;
import org.acme.util.mappers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AlumniServiceTest {
    @Mock
    AlumniRepository alumniRepository;
    @Mock
    AlumniDetailsRepository alumniDetailsRepository;
    @Mock
    FacultyService facultyService;
    @Mock
    DegreeService degreeService;
    @Mock
    AlumniGroupService groupService;
    @Spy
    AlumniMapper alumniMapper = new AlumniMapper();
    @Spy
    AlumniGroupsMembershipMapper membershipMapper = new AlumniGroupsMembershipMapper();

    @InjectMocks
    AlumniServiceImpl alumniService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        alumniMapper.degreeMapper = new DegreeMapper();
        alumniMapper.membershipMapper = new AlumniGroupsMembershipMapper();
    }

    @Test
    void getAlumniById_validId_returnsDto() throws Exception {
        Alumni alumni = new Alumni();
        alumni.setFacultyNumber(22221111);
        alumni.setFacebookUrl("https://facebook.com");
        alumni.setLinkedInUrl("https://linkedin.com");

        Degree degree = new Degree();
        degree.setId(2);
        degree.setDegree("BcS");
        alumni.setDegree(degree);

        AlumniGroupsMembership m = new AlumniGroupsMembership();
        m.setId(1);
        m.setAlumni(alumni);
        alumni.setMemberships(new ArrayList<>(List.of(m)));

        when(alumniRepository.findByIdOptional((long) 22221111))
            .thenReturn(Optional.of(alumni));

        AlumniDto result = alumniService.getAlumniById(22221111);

        assertNotNull(result);
        assertEquals(22221111, result.getFacultyNumber());
        assertEquals("https://facebook.com", result.getFacebookUrl());
        assertEquals("https://linkedin.com", result.getLinkedinUrl());
        assertEquals("BcS", result.getDegree());
        assertEquals(1, result.getMemberships().size());
        assertEquals(m.getId(), result.getMemberships().get(0).getId());
    }

    @Test
    void getAlumniById_invalidId_throwsException(){
        assertThrows(IncorrectAlumnusNumberException.class, () -> {
            alumniService.getAlumniById(0);
        });
    }

    @Test
    void getAlumniById_idNotFound_throwsException(){
        when(alumniRepository.findByIdOptional((long) 2)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            alumniService.getAlumniById(2);
        });
    }

    @Test
    void getAllAlumni_returnsDtoList() throws Exception {
        Degree degree = new Degree();
        degree.setId(2);
        degree.setDegree("BcS");

        Alumni alumni1 = new Alumni();
        alumni1.setFacultyNumber(22221111);
        Alumni alumni2 = new Alumni();
        alumni2.setFacultyNumber(22221112);
        alumni1.setDegree(degree);
        alumni2.setDegree(degree);

        AlumniGroupsMembership m = new AlumniGroupsMembership();
        m.setId(1);
        m.setAlumni(alumni1);
        alumni1.setMemberships(new ArrayList<>(List.of(m)));

        AlumniGroupsMembership m1 = new AlumniGroupsMembership();
        m1.setId(2);
        m1.setAlumni(alumni2);
        alumni2.setMemberships(new ArrayList<>(List.of(m1)));


        when(alumniRepository.listAll()).thenReturn(List.of(alumni1, alumni2));

        List<AlumniDto> resultList = alumniService.getAllAlumni();

        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals(22221111, resultList.get(0).getFacultyNumber());
        assertEquals(22221112, resultList.get(1).getFacultyNumber());
        assertEquals(1, resultList.get(0).getMemberships().size());
        assertEquals(1, resultList.get(1).getMemberships().size());
    }

    @Test
    void getAllAlumniGroups_emptyList(){
        when(alumniRepository.listAll()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            alumniService.getAllAlumni();
        });
    }

    @Test
    void createAlumni_success(){
        AlumniDto inputDto = new AlumniDto();
        degreeDto.setDegree("Bachelor");
        inputDto.setDegreeDto(degreeDto);

        AlumniGroupsMembershipDto mDto = new AlumniGroupsMembershipDto();
        mDto.setGroupNumber(123);
        inputDto.setMemberships(new ArrayList<>(List.of(mDto)));
        AlumniGroupsMembership m = new AlumniGroupsMembership();

        Degree degree = new Degree();
        degree.setId(2);
        degree.setDegree("Bachelor");
        Alumni alumniEntity = new Alumni();
        alumniEntity.setFacultyNumber(22221111);
        alumniEntity.setDegree(degree);
        alumniEntity.setMemberships(new ArrayList<>(List.of(m)));
        AlumniGroup group = new AlumniGroup();
        group.setId(1);
        group.setGroupNumber(678);

        m.setGroup(group);
        m.setAlumni(alumniEntity);

        when(degreeService.getDegreeByNameE("Bachelor")).thenReturn(degree);
        when(groupService.getAlumniGroupByGroupNumberE(123)).thenReturn(group);
        doNothing().when(alumniRepository).persist(any(Alumni.class));

        AlumniDto result = alumniService.createAlumni(inputDto);

        assertNotNull(result);
        verify(degreeService).getDegreeByNameE("Bachelor");
        verify(groupService).getAlumniGroupByGroupNumberE(123);

        assertEquals(1, alumniEntity.getMemberships().size());
        assertEquals(mDto.getId(), alumniEntity.getMemberships().get(0).getId());
    }

    @Test
    void updateAlumni_success(){}

    @Test
    void deleteAlumni_validId_returnsDto() throws Exception {
        Alumni alumni = new Alumni();
        alumni.setFacultyNumber(22221111);
        alumni.setFacebookUrl("facebook");

        Degree degree = new Degree();
        degree.setId(2);
        degree.setDegree("BcS");
        alumni.setDegree(degree);

        AlumniGroupsMembership m = new AlumniGroupsMembership();
        m.setId(1);
        m.setAlumni(alumni);
        alumni.setMemberships(new ArrayList<>(List.of(m)));

        when(alumniRepository.findByIdOptional((long) 22221111))
                .thenReturn(Optional.of(alumni));
        doNothing().when(alumniRepository).delete(alumni);

        AlumniDto result = alumniService.deleteAlumni(22221111);

        assertNotNull(result);
        assertEquals(22221111, result.getFacultyNumber());
        assertEquals("facebook", result.getFacebookUrl());
        assertEquals(1, result.getMemberships().size());
        assertEquals(m.getId(), result.getMemberships().get(0).getId());
        verify(alumniRepository, times(1)).delete(alumni);
    }

    @Test
    void deleteAlumni_invalidId_throwsException(){
        assertThrows(IncorrectAlumnusNumberException.class, () -> {
            alumniService.getAlumniById(-5);
        });
    }

    @Test
    void deleteAlumniGroup_idNotFound_throwsException(){
        when(alumniRepository.findByIdOptional((long) 3)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            alumniService.deleteAlumni(3);
        });
    }

    @Test
    void createAlumniDetails_success() throws Exception {
        AlumniDetailsDto dto = new AlumniDetailsDto();
        dto.setFaculty(1);
        dto.setFullName("FullName");
        dto.setBirthDate(LocalDate.of(2025,4,1));

        Faculty faculty = new Faculty();
        faculty.setId(1);

        when(facultyService.getFacultyByIdE(1)).thenReturn(faculty);
        doNothing().when(alumniRepository).persist(any(Alumni.class));

        AlumniDetailsDto result = alumniService.createAlumniDetails(dto);

        assertNotNull(result);
        assertEquals(1, result.getFaculty());
        assertEquals("FullName", result.getFullName());
        assertEquals(LocalDate.of(2025,4,1), result.getBirthDate());
        assertEquals(faculty.getId(), result.getFaculty());
        verify(alumniDetailsRepository, times(1))
                .persist(any(AlumniDetails.class));
    }

    @Test
    void getAlumniDetails_validId_returnsDto() throws Exception {
        int alumniId = 10;
        AlumniDetails details = new AlumniDetails();
        details.setFacultyNumber(alumniId);
        details.setFullName("FullName");
        details.setBirthDate(LocalDate.of(2025,4,1));

        Faculty faculty = new Faculty();
        faculty.setId(1);
        faculty.setFacultyName("FacultyName");

        details.setFaculty(faculty);

        when(alumniDetailsRepository.findByIdOptional((long) alumniId))
                .thenReturn(Optional.of(details));

        AlumniDetailsDto result = alumniService.getAlumniDetails(alumniId);

        assertNotNull(result);
        assertEquals(alumniId, result.getFacultyNumber());
        assertEquals("FullName", result.getFullName());
        assertEquals(LocalDate.of(2025,4,1), result.getBirthDate());
        assertEquals(faculty.getId(), result.getFaculty());
    }

    @Test
    void getAlumniDetails_invalidId_throwsException() throws Exception {
        assertThrows(IncorrectAlumnusNumberException.class, () -> {
            alumniService.getAlumniDetails(-1);
        });
    }

    @Test
    void getAlumniDetails_idNotFound_throwsException() throws Exception {
        when(alumniRepository.findByIdOptional((long) 2)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            alumniService.getAlumniDetails(2);
        });
    }


    @Test
    void updateAlumniDetails_validInput_updatesAndReturnsDto() throws Exception {
        int alumniId = 5;
        Faculty faculty = new Faculty();
        faculty.setId(1);
        faculty.setFacultyName("FacultyName");

        AlumniDetails existingDetails = new AlumniDetails();
        existingDetails.setFacultyNumber(alumniId);
        existingDetails.setFaculty(faculty);
        existingDetails.setFullName("FullName");
        existingDetails.setBirthDate(LocalDate.of(2025,4,1));

        AlumniDetailsDto dto = new AlumniDetailsDto();
        dto.setFacultyNumber(alumniId);
        dto.setFaculty(1);
        dto.setFullName("FullName");
        dto.setBirthDate(LocalDate.of(2025,4,1));

        when(alumniDetailsRepository.findByIdOptional((long) alumniId)).thenReturn(Optional.of(existingDetails));
        when(facultyService.getFacultyByIdE(faculty.getId())).thenReturn(faculty);
        doNothing().when(alumniDetailsRepository).persist(existingDetails);

        AlumniDetailsDto result = alumniService.updateAlumniDetails(alumniId, dto);

        assertNotNull(result);
        assertEquals(alumniId, result.getFacultyNumber());
        assertEquals("FullName", result.getFullName());
        assertEquals(1, result.getFaculty());
        assertEquals(LocalDate.of(2025,4,1), result.getBirthDate());

        verify(alumniDetailsRepository, times(1)).persist(existingDetails);
    }

    @Test
    void updateAlumniDetails_invalidId_throwsException() {
        int invalidId = -1;
        AlumniDetailsDto dto = new AlumniDetailsDto();

        assertThrows(IncorrectAlumnusNumberException.class, () -> {
            alumniService.updateAlumniDetails(invalidId, dto);
        });
    }

    @Test
    void updateAlumniDetails_idNotFound_throwsException() {
        int alumniId = 123;

        when(alumniDetailsRepository.findByIdOptional((long) alumniId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            alumniService.updateAlumniDetails(alumniId, new AlumniDetailsDto());
        });
    }
}
