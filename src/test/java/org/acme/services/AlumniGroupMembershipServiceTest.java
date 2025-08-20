package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.avro.front.AlumniGroupMembershipFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;
import org.acme.entites.Degree;
import org.acme.entites.Faculty;
import org.acme.entites.Speciality;
import org.acme.repository.AlumniGroupRepository;
import org.acme.repository.AlumniGroupsMembershipRepository;
import org.acme.repository.AlumniRepository;
import org.acme.service.group_service.AlumniGroupMembershipServiceImpl;
import org.acme.util.mappers.AlumniGroupCommonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class AlumniGroupMembershipServiceTest {

    @InjectMock
    private AlumniGroupsMembershipRepository membershipRepository;
    @InjectMock
    private AlumniRepository alumniRepository;
    @InjectMock
    private AlumniGroupRepository groupRepository;

    @Inject
    private AlumniGroupCommonMapper groupMembershipMapper;
    @Inject
    private AlumniGroupMembershipServiceImpl service;

    private AlumniGroupsMembership membership;
    private AlumniGroupMembershipFrontDto frontDto;
    private AlumniGroupMembershipDto dto;
    private Faculty f;
    private Speciality s;
    private Alumni a;
    private Degree d;
    private AlumniGroupBackDto groupDto;

    @BeforeEach
    void setUp() {
    MockitoAnnotations.openMocks(this);

    // Alumni and Degree
    a = new Alumni();
    d = new Degree();
    d.setId(1);
    d.setDegree("bachelor"); 
    a.setFacultyNumber(101);
    a.setDegree(d);

    // Faculty and Speciality
    f = new Faculty();
    f.setId(1);
    f.setFacultyName("FITA");

    s = new Speciality();
    s.setId(1);
    s.setSpecialityName("SIT");

    // Membership
    membership = new AlumniGroupsMembership();
    membership.setId(1);
    membership.setAverageScore(3.5);

    // AlumniGroup
    AlumniGroup g = new AlumniGroup();
    g.setId(1);
    g.setGroupNumber(202);
    g.setFaculty(f);
    g.setSpeciality(s);
    g.setGraduationYear(2000);
    g.setMemberships(List.of(membership));

    a.setMemberships(List.of(membership));
    membership.setAlumni(a);
    membership.setGroup(g);

    // Front DTO
    frontDto = new AlumniGroupMembershipFrontDto();
    frontDto.setId(1);
    frontDto.setAverageScore(3.5);
    frontDto.setFacultyNumber(101);
    frontDto.setGroupNumber(202);

    // Back DTO
    dto = new AlumniGroupMembershipDto();
    dto.setId(1);
    dto.setAverageScore(3.5);
    dto.setFacultyNumber(101);

    dto.setGroup(groupMembershipMapper.toAlumniGroupDto(g));
}



    // -------- createAlumniGroupsMembership(FrontDto) --------

    @Test
    void createFromFrontDto_nullDto_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> service.createAlumniGroupsMembership((AlumniGroupMembershipFrontDto) null));
        assertEquals("Membership DTO is null.", ex.getMessage());
    }

    @Test
    void createFromFrontDto_groupNotFound_throwsException() {
        groupMembershipMapper.toEntityFront(frontDto);
        when(groupRepository.findByIdOptional(202L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> service.createAlumniGroupsMembership(frontDto));
        assertEquals("AlumniGroup not found.", ex.getMessage());
    }

    @Test
    void createFromFrontDto_alumniNotFound_throwsException() {
        // when(groupMembershipMapper.toEntityFront(frontDto)).thenReturn(membership);
        groupMembershipMapper.toEntityFront(frontDto);
        when(groupRepository.findByIdOptional(202L)).thenReturn(Optional.of(new AlumniGroup()));
        when(alumniRepository.findByIdOptional(101L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> service.createAlumniGroupsMembership(frontDto));
        assertEquals("Alumni with faculty number 101 was not found", ex.getMessage());
    }

    @Test
    void createFromFrontDto_success_persistsAndReturns() throws Exception {
        // Stub repositories to return existing entities
        when(groupRepository.findByIdOptional(202L)).thenReturn(Optional.of(membership.getGroup()));
        when(alumniRepository.findByIdOptional(101L)).thenReturn(Optional.of(membership.getAlumni()));

        // Call the service
        AlumniGroupsMembership result = service.createAlumniGroupsMembership(frontDto);

        // Verify repository persist was called with an object having same properties
        verify(membershipRepository).persist(any(AlumniGroupsMembership.class));

        // Assert that the returned object has the same id as expected
        assertSame(membership.getId(), result.getId());
        assertEquals(membership.getAverageScore(), result.getAverageScore());
        assertSame(membership.getGroup(), result.getGroup());
        assertSame(membership.getAlumni(), result.getAlumni());
        
    }

    // -------- createAlumniGroupsMembership(Entity) --------

    @Test
    void createFromEntity_null_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> service.createAlumniGroupsMembership((AlumniGroupsMembership) null));
        assertEquals("Membership entity is null.", ex.getMessage());
    }

    @Test
    void createFromEntity_success_persists() throws Exception {
        AlumniGroupsMembership result = service.createAlumniGroupsMembership(membership);
        verify(membershipRepository).persist(membership);
        assertSame(membership, result);
    }

    // -------- deleteAlumniGroupsMembership(Integer) --------

    @Test
    void deleteById_invalidId_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> service.deleteAlumniGroupsMembership(0));
        assertEquals("Invalid membership ID.", ex.getMessage());
    }

    @Test
    void deleteById_notFound_throwsException() {
        when(membershipRepository.deleteById(1L)).thenReturn(false);

        Exception ex = assertThrows(Exception.class, () -> service.deleteAlumniGroupsMembership(1));
        assertEquals("Membership with ID 1 not found.", ex.getMessage());
    }

    @Test
    void deleteById_success() throws Exception {
        when(membershipRepository.deleteById(1L)).thenReturn(true);
        service.deleteAlumniGroupsMembership(1);
        verify(membershipRepository).deleteById(1L);
    }

    // -------- deleteAlumniGroupsMembership(FrontDto) --------

    @Test
    void deleteByFrontDto_nullDto_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> service.deleteAlumniGroupsMembership((AlumniGroupMembershipFrontDto) null));
        assertEquals("DTO is null.", ex.getMessage());
    }

    @Test
    void deleteByFrontDto_delegatesToIdDelete() throws Exception {
        when(membershipRepository.deleteById(1L)).thenReturn(true);
        service.deleteAlumniGroupsMembership(frontDto);
        verify(membershipRepository).deleteById(1L);
    }

    // -------- deleteAlumniGroupsMembership(Entity) --------

    @Test
    void deleteByEntity_nullEntity_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> service.deleteAlumniGroupsMembership((AlumniGroupsMembership) null));
        assertEquals("Invalid membership.", ex.getMessage());
    }

    @Test
    void deleteByEntity_nullId_throwsException() {
        membership.setId(null);
        Exception ex = assertThrows(Exception.class, () -> service.deleteAlumniGroupsMembership(membership));
        assertEquals("Invalid membership.", ex.getMessage());
    }

    @Test
    void deleteByEntity_delegatesToIdDelete() throws Exception {
        when(membershipRepository.deleteById(1L)).thenReturn(true);
        service.deleteAlumniGroupsMembership(membership);
        verify(membershipRepository).deleteById(1L);
    }

    // -------- getAll... --------

    // tobackdtos in groupmembershipmapper returns list WITHOUT GROUP, because it does not populate the group (did not!)
    @Test
    void getAllByFacultyNumber_returnsMappedDtos() throws Exception {
        when(membershipRepository.findAllByFacultyNumber(101)).thenReturn(Collections.singletonList(membership));
        when(groupRepository.findByIdOptional(202L)).thenReturn(Optional.of(membership.getGroup()));
        // do not stub services
        List<AlumniGroupMembershipDto> result = service.getAllAlumniGroupMembershipsByFacultyNumber(101);
        assertEquals(1, result.size());
    }

    @Test
    void getAllByGroup_returnsMappedDtos() throws Exception {
        when(membershipRepository.findAllByGroupId(202)).thenReturn(Collections.singletonList(membership));
        // when(groupMembershipMapper.toBackDtos(anyList())).thenReturn(Arrays.asList(new AlumniGroupMembershipDto()));

        List<AlumniGroupMembershipDto> result = service.getAllAlumniGroupMembershipsByGroup(202);
        assertEquals(1, result.size());
    }

    // -------- getById --------

    @Test
    void getById_invalidId_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> service.getAlumniGroupsMembershipById(0));
        assertEquals("Invalid ID.", ex.getMessage());
    }

    @Test
    void getById_notFound_throwsException() {
        when(membershipRepository.findById(1L)).thenReturn(null);

        Exception ex = assertThrows(Exception.class, () -> service.getAlumniGroupsMembershipById(1));
        assertEquals("Membership not found.", ex.getMessage());
    }

    @Test
    void getById_success_returnsEntity() throws Exception {
        when(membershipRepository.findById(1L)).thenReturn(membership);
        AlumniGroupsMembership result = service.getAlumniGroupsMembershipById(1);
        assertSame(membership, result);
    }

    // -------- updateFromFrontDto --------

    @Test
    void updateFromFrontDto_invalidDto_throwsException() {
        AlumniGroupMembershipFrontDto invalid = new AlumniGroupMembershipFrontDto();
        invalid.setId(0);
        Exception ex = assertThrows(Exception.class, () -> service.updateAlumniGroupsMembership(invalid));
        assertEquals("Invalid DTO.", ex.getMessage());
    }

    @Test
    void updateFromFrontDto_noExistingMembership_throwsException() {
        when(membershipRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> service.updateAlumniGroupsMembership(frontDto));
        assertEquals("No such group membership was found.", ex.getMessage());
    }

    @Test
    void updateFromFrontDto_noAlumniFound_throwsException() {
        when(membershipRepository.findByIdOptional(1L)).thenReturn(Optional.of(membership));
        when(alumniRepository.findByIdOptional(101L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> service.updateAlumniGroupsMembership(frontDto));
        assertEquals("No such alumni was found.", ex.getMessage());
    }

    @Test
    void updateFromFrontDto_noGroupFound_throwsException() {
        when(membershipRepository.findByIdOptional(1L)).thenReturn(Optional.of(membership));
        when(alumniRepository.findByIdOptional(101L)).thenReturn(Optional.of(new Alumni()));
        when(groupRepository.findByIdOptional(202L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> service.updateAlumniGroupsMembership(frontDto));
        assertEquals("No group with such id number was found.", ex.getMessage());
    }

    @Test
    void updateFromFrontDto_success_persistsAndReturns() throws Exception {
        when(membershipRepository.findByIdOptional(1L)).thenReturn(Optional.of(membership));
        when(alumniRepository.findByIdOptional(101L)).thenReturn(Optional.of(new Alumni()));
        when(groupRepository.findByIdOptional(202L)).thenReturn(Optional.of(new AlumniGroup()));

        AlumniGroupsMembership result = service.updateAlumniGroupsMembership(frontDto);

        verify(membershipRepository).persist(membership);
        assertSame(membership, result);
    }

    // -------- updateFromEntity --------

    @Test
    void updateFromEntity_nullEntity_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> service.updateAlumniGroupsMembership((AlumniGroupsMembership) null));
        assertEquals("Invalid membership.", ex.getMessage());
    }

    @Test
    void updateFromEntity_nullId_throwsException() {
        membership.setId(null);
        Exception ex = assertThrows(Exception.class, () -> service.updateAlumniGroupsMembership(membership));
        assertEquals("Invalid membership.", ex.getMessage());
    }

    @Test
    void updateFromEntity_noExistingMembership_throwsException() {
        when(membershipRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> service.updateAlumniGroupsMembership(membership));
        assertEquals("No such group membership was found.", ex.getMessage());
    }

    @Test
    void updateFromEntity_success_persistsAndReturns() throws Exception {
        when(membershipRepository.findByIdOptional(1L)).thenReturn(Optional.of(new AlumniGroupsMembership()));

        AlumniGroupsMembership result = service.updateAlumniGroupsMembership(membership);

        verify(membershipRepository).persist(any(AlumniGroupsMembership.class));
        assertNotNull(result);
    }
}
