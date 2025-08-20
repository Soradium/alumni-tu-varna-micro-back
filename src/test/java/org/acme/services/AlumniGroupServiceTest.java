package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.acme.avro.ambiguous.AlumniGroupDtoSimplified;
import org.acme.avro.ambiguous.FacultyDto;
import org.acme.avro.ambiguous.SpecialityDto;
import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;
import org.acme.repository.AlumniGroupRepository;
import org.acme.service.group_service.AlumniGroupServiceImpl;
import org.acme.util.mappers.AlumniGroupMapper;
import org.acme.util.mappers.FacultyMapper;
import org.acme.util.mappers.SpecialityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class AlumniGroupServiceTest {

    @InjectMock
    private AlumniGroupRepository groupRepository;

    @Inject
    private AlumniGroupMapper groupMapper;

    @Inject
    private FacultyMapper facultyMapper;

    @Inject
    private SpecialityMapper specialityMapper;

    @Inject
    private AlumniGroupServiceImpl service;

    private AlumniGroup group;
    private AlumniGroupDtoSimplified dto;
    private AlumniGroupsMembership membership;

    @BeforeEach
    void setUp() {
        group = new AlumniGroup();
        group.setId(1);

        dto = new AlumniGroupDtoSimplified();
        dto.setId(1);
        dto.setGraduationYear(2020);
        dto.setGroupNumber(101);
        dto.setFaculty(new FacultyDto());
        dto.setSpeciality(new SpecialityDto());

        membership = new AlumniGroupsMembership();
        membership.setId(1);
        membership.setGroup(group);

        group.setMemberships(List.of(membership));
    }

    // -------- createAlumniGroup(dto) --------
    @Test
    void createAlumniGroup_fromDto_success() throws Exception {

        AlumniGroup result = service.createAlumniGroup(dto);

        verify(groupRepository).persist(any(AlumniGroup.class));
        assertEquals(group.getId(), result.getId());
    }

    @Test
    void createAlumniGroup_fromDto_nullDto_throwsException() {
        assertThrows(NullPointerException.class, () -> service.createAlumniGroup((AlumniGroupDtoSimplified) null));
    }

    // -------- createAlumniGroup(entity) --------
    @Test
    void createAlumniGroup_fromEntity_success() throws Exception {
        AlumniGroup result = service.createAlumniGroup(group);

        assertEquals(group, result);
        verify(groupRepository).persist(group);
    }

    @Test
    void createAlumniGroup_fromEntity_nullEntity_throwsException() {
        assertThrows(NullPointerException.class, () -> service.createAlumniGroup((AlumniGroup) null));
    }

    // -------- deleteAlumniGroup(id) --------
    @Test
    void deleteAlumniGroup_byId_success() throws Exception {
        service.deleteAlumniGroup(1);
        verify(groupRepository).deleteById(1L);
    }

    @Test
    void deleteAlumniGroup_byId_null_throwsException() {
        assertThrows(NullPointerException.class, () -> service.deleteAlumniGroup((Integer) null));
    }

    // -------- deleteAlumniGroup(dto) --------
    @Test
    void deleteAlumniGroup_byDto_success() throws Exception {
        service.deleteAlumniGroup(dto);
        verify(groupRepository).deleteById(1L);
    }

    @Test
    void deleteAlumniGroup_byDto_nullDto_throwsException() {
        assertThrows(NullPointerException.class, () -> service.deleteAlumniGroup((AlumniGroupDtoSimplified) null));
    }

    // -------- deleteAlumniGroup(entity) --------
    @Test
    void deleteAlumniGroup_byEntity_success() throws Exception {
        service.deleteAlumniGroup(group);
        verify(groupRepository).delete(group);
    }

    @Test
    void deleteAlumniGroup_byEntity_nullEntity_throwsException() {
        assertThrows(NullPointerException.class, () -> service.deleteAlumniGroup((AlumniGroup) null));
    }

    // -------- getAllAlumniGroupsDtoByFaculty --------
    @Test
    void getAllByFaculty_emptyList_returnsEmpty() throws Exception {
        PanacheQuery<AlumniGroup> query = mock(PanacheQuery.class);

        when(groupRepository.find("faculty.facultyName", "Science")).thenReturn(query);
        when(query.list()).thenReturn(Collections.emptyList());
        

        List<AlumniGroupBackDto> result = service.getAllAlumniGroupsDtoByFaculty("Science");
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllByFaculty_nullFaculty_throwsException() {
        assertThrows(NullPointerException.class, () -> service.getAllAlumniGroupsDtoByFaculty(null));
    }

    // -------- getAllAlumniGroupsDtoByGraduationYear --------
    @Test
    void getAllByGraduationYear_noResults_returnsEmpty() throws Exception {
        PanacheQuery<AlumniGroup> query = mock(PanacheQuery.class);

        when(groupRepository.find("graduationYear", 2020)).thenReturn(query);
        when(query.list()).thenReturn(Collections.emptyList());

        List<AlumniGroupBackDto> result = service.getAllAlumniGroupsDtoByGraduationYear(2020);
        assertTrue(result.isEmpty());
    }

    // -------- getAllAlumniGroupsDtoBySpeciality --------
    @Test
    void getAllBySpeciality_noResults_returnsEmpty() throws Exception {
        PanacheQuery<AlumniGroup> query = mock(PanacheQuery.class);

        when(groupRepository.find("speciality.specialityName", "Math")).thenReturn(query);
        when(query.list()).thenReturn(Collections.emptyList());
        
        List<AlumniGroupBackDto> result = service.getAllAlumniGroupsDtoBySpeciality("Math");
        assertTrue(result.isEmpty());
    }

    // -------- getAlumniGroupById --------
    @Test
    void getById_found_returnsEntity() throws Exception {
        when(groupRepository.findByIdOptional(1L)).thenReturn(Optional.of(group));
        AlumniGroup result = service.getAlumniGroupById(1);
        assertEquals(group, result);
    }

    @Test
    void getById_notFound_throwsException() {
        when(groupRepository.findByIdOptional(1L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> service.getAlumniGroupById(1));
    }

    // -------- updateAlumniGroup(dto) --------
    @Test
    void updateFromDto_success() throws Exception {
        when(groupRepository.findByIdOptional(1L)).thenReturn(Optional.of(group));

        AlumniGroup updated = service.updateAlumniGroup(dto);

        assertEquals(dto.getGraduationYear(), updated.getGraduationYear());
        verify(groupRepository).persist(group);
    }

    @Test
    void updateFromDto_notFound_throwsException() {
        when(groupRepository.findByIdOptional(1L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> service.updateAlumniGroup(dto));
    }

    // -------- updateAlumniGroup(entity) --------
    @Test
    void updateFromEntity_success() throws Exception {
        when(groupRepository.findByIdOptional(1L)).thenReturn(Optional.of(new AlumniGroup()));

        AlumniGroup updated = service.updateAlumniGroup(group);

        assertEquals(group.getGraduationYear(), updated.getGraduationYear());
        verify(groupRepository).persist(updated);
    }

    @Test
    void updateFromEntity_notFound_throwsException() {
        when(groupRepository.findByIdOptional(1L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> service.updateAlumniGroup(group));
    }

    // -------- getAlumniGroupDtoById --------
    @Test
    void getDtoById_found_returnsDto() throws Exception {
        AlumniGroupBackDto dtoBack = new AlumniGroupBackDto();
        dtoBack.setId(1);
        when(groupRepository.findByIdOptional(1L)).thenReturn(Optional.of(group));

        AlumniGroupBackDto result = service.getAlumniGroupDtoById(1);
        assertEquals(dtoBack.getId(), result.getId());
    }

    @Test
    void getDtoById_notFound_throwsException() {
        when(groupRepository.findByIdOptional(1L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> service.getAlumniGroupDtoById(1));
    }
}
