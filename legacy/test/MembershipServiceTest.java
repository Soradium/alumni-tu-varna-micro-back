package org.acme.services;

import org.acme.dto.AlumniGroupsMembershipDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.AlumniGroupsMembershipRepository;
import org.acme.service.AlumniGroupService;
import org.acme.service.AlumniService;
import org.acme.service.implementation.MembershipServiceImpl;
import org.acme.util.mappers.AlumniGroupsMembershipMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MembershipServiceTest {
    @Mock
    AlumniGroupsMembershipRepository repository;
    @Mock
    AlumniService alumniService;
    @Mock
    AlumniGroupService alumniGroupService;
    @Spy
    AlumniGroupsMembershipMapper mapper = new AlumniGroupsMembershipMapper();

    @InjectMocks
    MembershipServiceImpl service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMembershipByFN_validId_returnsDto() {
        int id = 123;
        Alumni alumni = new Alumni();
        alumni.setFacultyNumber(id);
        AlumniGroup alumniGroup = new AlumniGroup();
        alumniGroup.setGroupNumber(2);

        AlumniGroupsMembership entity = new AlumniGroupsMembership();
        entity.setId(id);
        entity.setAlumni(alumni);
        entity.setGroup(alumniGroup);
        entity.setAverageScore(3.64);

        when(repository.findByFNOptional(id)).thenReturn(Optional.of(entity));

        AlumniGroupsMembershipDto result = service.getMembershipByFN(id);

        assertNotNull(result);
        assertEquals(id, result.getFacultyNumber());
        assertEquals(2, result.getGroupNumber());
        assertEquals(3.64, result.getAverageScore());
    }

    @Test
    void getMembershipByFN_idNotFound_throwsException() {
        when(repository.findByFNOptional(123)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getMembershipByFN(123));
    }

    @Test
    void getAllMemberships_success() {
        Alumni alumni1 = new Alumni();
        alumni1.setFacultyNumber(123);

        Alumni alumni2 = new Alumni();
        alumni2.setFacultyNumber(321);

        AlumniGroup alumniGroup1 = new AlumniGroup();
        alumniGroup1.setGroupNumber(1);

        AlumniGroup alumniGroup2 = new AlumniGroup();
        alumniGroup2.setGroupNumber(2);

        AlumniGroupsMembership m1 = new AlumniGroupsMembership();
        AlumniGroupsMembership m2 = new AlumniGroupsMembership();
        m1.setId(555);
        m2.setId(666);
        m1.setAlumni(alumni1);
        m2.setAlumni(alumni2);
        m1.setGroup(alumniGroup1);
        m2.setGroup(alumniGroup2);

        when(repository.listAll()).thenReturn(List.of(m1, m2));

        List<AlumniGroupsMembershipDto> result = service.getAllMemberships();

        assertEquals(2, result.size());
        assertEquals(555, result.get(0).getId());
        assertEquals(1, result.get(0).getGroupNumber());
        assertEquals(666, result.get(1).getId());
        assertEquals(2, result.get(1).getGroupNumber());
    }

    @Test
    void getAllMemberships_emptyList_throwsException() {
        when(repository.listAll()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> service.getAllMemberships());
    }

    @Test
    void createMembership_validInput_returnsSavedDto() throws Exception {
        AlumniGroupsMembershipDto dto = new AlumniGroupsMembershipDto();
        dto.setFacultyNumber(101);
        dto.setGroupNumber(202);

        Alumni alumni = new Alumni();
        alumni.setFacultyNumber(101);
        AlumniGroup group = new AlumniGroup();
        group.setGroupNumber(202);

        when(alumniService.getAlumniByIdE(101)).thenReturn(alumni);
        when(alumniGroupService.getAlumniGroupByGroupNumberE(202)).thenReturn(group);
        doNothing().when(repository).persist(any(AlumniGroupsMembership.class));

        AlumniGroupsMembershipDto result = service.createMembership(dto);

        assertNotNull(result);
        assertEquals(dto.getFacultyNumber(), result.getFacultyNumber());
    }

    @Test
    void updateMembership_validId_updatesAndReturnsDto() {
        int id = 123;
        AlumniGroupsMembership existing = new AlumniGroupsMembership();

        Alumni a = new Alumni();
        a.setFacultyNumber(id);
        existing.setAlumni(a);

        AlumniGroupsMembershipDto inputDto = new AlumniGroupsMembershipDto();
        inputDto.setAverageScore(5.5);

        when(repository.findByFNOptional(id)).thenReturn(Optional.of(existing));
        doNothing().when(repository).persist(any(AlumniGroupsMembership.class));

        AlumniGroupsMembershipDto result = service.updateMembership(id, inputDto);

        assertEquals(5.5, result.getAverageScore());
    }

    @Test
    void updateMembership_idNotFound_throwsException() {
        when(repository.findByFNOptional(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateMembership(999, new AlumniGroupsMembershipDto()));
    }

    @Test
    void deleteMembership_validId_success() {
        int id = 123;
        Alumni alumni = new Alumni();
        alumni.setFacultyNumber(id);
        AlumniGroup alumniGroup = new AlumniGroup();
        alumniGroup.setGroupNumber(2);

        AlumniGroupsMembership entity = new AlumniGroupsMembership();
        entity.setId(id);
        entity.setAlumni(alumni);
        entity.setGroup(alumniGroup);
        entity.setAverageScore(3.64);

        when(repository.findByFNOptional(id)).thenReturn(Optional.of(entity));
        doNothing().when(repository).delete(entity);

        AlumniGroupsMembershipDto result = service.deleteMembership(id);

        assertNotNull(result);
        verify(repository).delete(entity);
    }

    @Test
    void deleteMembership_notFound_throwsException() {
        when(repository.findByFNOptional(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.deleteMembership(999));
    }

}
