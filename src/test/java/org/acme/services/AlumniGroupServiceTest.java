package org.acme.services;

import jakarta.inject.Inject;
import org.acme.dto.AlumniGroupDto;
import org.acme.dto.AlumniGroupsMembershipDto;
import org.acme.dto.FacultyDto;
import org.acme.dto.SpecialityDto;
import org.acme.entites.*;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.AlumniGroupRepository;
import org.acme.service.AlumniService;
import org.acme.service.FacultyService;
import org.acme.service.SpecialityService;
import org.acme.service.implementation.AlumniGroupServiceImpl;
import org.acme.util.mappers.AlumniGroupMapper;
import org.acme.util.mappers.AlumniGroupsMembershipMapper;
import org.acme.util.mappers.FacultyMapper;
import org.acme.util.mappers.SpecialityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AlumniGroupServiceTest {
    @Mock
    AlumniGroupRepository alumniGroupRepository;
    @Mock
    SpecialityService specialityService;
    @Mock
    FacultyService facultyService;
    @Mock
    AlumniService alumniService;

    @Spy
    AlumniGroupMapper groupMapper = new AlumniGroupMapper();

    @InjectMocks
    AlumniGroupServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        groupMapper.facultyMapper = new FacultyMapper();
        groupMapper.specialityMapper = new SpecialityMapper();
        groupMapper.membershipMapper = new AlumniGroupsMembershipMapper();
    }

    @Test
    void testGetAlumniGroupByGroupNumber_success() {
        Faculty f = new Faculty();
        f.setId(2);
        f.setFacultyName("facultyName");

        Speciality s = new Speciality();
        s.setId(1);
        s.setSpecialityName("specialityName");

        Alumni a = new Alumni();
        a.setFacultyNumber(22221111);

        AlumniGroupsMembership m = new AlumniGroupsMembership();
        m.setId(1);
        m.setAlumni(a);
        ArrayList<AlumniGroupsMembership> memberships = new ArrayList<>();
        memberships.add(m);

        a.setMemberships(memberships);

        AlumniGroup group = new AlumniGroup();
        group.setGroupNumber(22);
        group.setFaculty(f);
        group.setSpeciality(s);
        group.setMemberships(memberships);

        when(alumniGroupRepository.findByGroupNumberOptional(22))
                .thenReturn(Optional.of(group));

        AlumniGroupDto result = service.getAlumniGroupByGroupNumber(22);

        assertEquals(22, result.getGroupNumber());
        assertEquals(f.getFacultyName(), result.getFaculty().getFacultyName());
        assertEquals(s.getSpecialityName(), result.getSpeciality().getSpeciality());
        assertEquals(memberships.getFirst().getId(), result.getMemberships().get(0).getId());
    }

    @Test
    void testGetAlumniGroupByGroupNumber_notFound() {
        when(alumniGroupRepository.findByGroupNumberOptional(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getAlumniGroupByGroupNumber(99));
    }

    @Test
    void testCreateAlumniGroup_success() throws Exception {
        AlumniGroupDto dto = new AlumniGroupDto();
        dto.setGroupNumber(123);

        Faculty faculty = new Faculty();
        faculty.setFacultyName("Engineering");
        FacultyDto facultyDto = new FacultyDto();
        facultyDto.setFacultyName("Engineering");

        Speciality speciality = new Speciality();
        speciality.setId(1);
        speciality.setSpecialityName("CS");
        SpecialityDto specialityDto = new SpecialityDto();
        specialityDto.setSpeciality("CS");

        dto.setFaculty(facultyDto);
        dto.setSpeciality(specialityDto);

        Alumni a = new Alumni();
        a.setFacultyNumber(22221111);

        AlumniGroupsMembershipDto mDto = new AlumniGroupsMembershipDto();
        mDto.setFacultyNumber(22221111);
        dto.setMemberships(new ArrayList<>(List.of(mDto)));

        AlumniGroupsMembership m = new AlumniGroupsMembership();
        m.setId(1);
        m.setAlumni(a);

        a.setMemberships(new ArrayList<>(List.of(m)));

        when(facultyService.getFacultyByNameE("Engineering")).thenReturn(faculty);
        when(specialityService.getSpecialityByNameE("CS")).thenReturn(speciality);
        when(alumniService.getAlumniByIdE(22221111)).thenReturn(a);

        doNothing().when(alumniGroupRepository).persist(any(AlumniGroup.class));

        AlumniGroupDto result = service.createAlumniGroup(dto);

        assertEquals(123, result.getGroupNumber());
        assertEquals(faculty.getFacultyName(), result.getFaculty().getFacultyName());
        assertEquals(speciality.getSpecialityName(), result.getSpeciality().getSpeciality());
        assertEquals(dto.getMemberships().getFirst().getId(), result.getMemberships().get(0).getId());
    }

    // createAlumniGroup()
    //test Faculty or Speciality not found, then throw exception
    //Alumni not found for a membership, throw exception

    @Test
    void testUpdateAlumniGroup_success() throws Exception {

    }
    //group not found

    @Test
    void testAssignToGroup_success() throws Exception {
        AlumniGroupsMembershipDto dto = new AlumniGroupsMembershipDto();
        dto.setFacultyNumber(22221111);
        dto.setGroupNumber(321);

        Faculty faculty = new Faculty();
        faculty.setId(2);
        faculty.setFacultyName("Engineering");
        Speciality speciality = new Speciality();
        speciality.setId(1);
        speciality.setSpecialityName("CS");

        Alumni alumni1 = new Alumni();
        alumni1.setFacultyNumber(22221111);

        Alumni alumni2 = new Alumni();
        alumni2.setFacultyNumber(22221112);

        AlumniGroupsMembership m = new AlumniGroupsMembership();
        m.setId(1);
        m.setAlumni(alumni1);

        AlumniGroupsMembership m2 = new AlumniGroupsMembership();
        m2.setId(2);
        m2.setAlumni(alumni2);

        AlumniGroup group = new AlumniGroup();
        group.setGroupNumber(321);
        group.setMemberships(new ArrayList<>(List.of(m2)));
        group.setFaculty(faculty);
        group.setSpeciality(speciality);
        alumni1.setMemberships(new ArrayList<>(List.of(m)));
        alumni2.setMemberships(new ArrayList<>(List.of(m2)));

        when(alumniService.getAlumniByIdE(22221111)).thenReturn(alumni1);
        when(alumniGroupRepository.findByGroupNumberOptional(321)).thenReturn(Optional.of(group));
        doNothing().when(alumniGroupRepository).persist(any(AlumniGroup.class));

        AlumniGroupDto result = service.assignToGroup(dto);

        assertEquals(321, result.getGroupNumber());
        assertEquals(2, group.getMemberships().size());
    }
    //Alumni/group not found, throw exception
    //Alumni already in group, then IllegalStateException


//
//    @Test
//    void testAssignToGroup_alreadyMember() throws Exception {
//
//    }
}
