package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.acme.entites.Faculty;
import org.acme.entites.Speciality;
import org.acme.repository.AlumniGroupRepository;
import org.acme.service.implementations.AlumniGroupServiceImpl;
import org.acme.util.mappers.AlumniGroupMapper;
import org.acme.util.mappers.FacultyMapper;
import org.acme.util.mappers.SpecialityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AlumniGroupServiceTest {

    @Mock
    private AlumniGroupRepository groupRepository;

    @Mock
    private AlumniGroupMapper groupMapper;

    @Mock
    private FacultyMapper facultyMapper;

    @Mock
    private SpecialityMapper specialityMapper;

    @InjectMocks
    private AlumniGroupServiceImpl service;

    private AlumniGroup group;
    private AlumniGroupDtoSimplified dto;

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
    }

    // -------- createAlumniGroup(dto) --------
    @Test
    void createAlumniGroup_fromDto_success() throws Exception {
        when(groupMapper.toEntitySimplified(dto)).thenReturn(group);

        AlumniGroup result = service.createAlumniGroup(dto);

        assertEquals(group, result);
        verify(groupRepository).persist(group);
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
        when(groupRepository.find("faculty.facultyName", "Science").list()).thenReturn(Collections.emptyList());
        when(groupMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

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
        when(groupRepository.find("graduationYear", 2020).list()).thenReturn(Collections.emptyList());
        when(groupMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<AlumniGroupBackDto> result = service.getAllAlumniGroupsDtoByGraduationYear(2020);
        assertTrue(result.isEmpty());
    }

    // -------- getAllAlumniGroupsDtoBySpeciality --------
    @Test
    void getAllBySpeciality_noResults_returnsEmpty() throws Exception {
        when(groupRepository.find("speciality.specialityName", "Math").list()).thenReturn(Collections.emptyList());
        when(groupMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

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
        when(facultyMapper.toEntity(dto.getFaculty())).thenReturn(new Faculty());
        when(specialityMapper.toEntity(dto.getSpeciality())).thenReturn(new Speciality());

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
        when(groupRepository.findByIdOptional(1L)).thenReturn(Optional.of(group));
        when(groupMapper.toDto(group)).thenReturn(dtoBack);

        AlumniGroupBackDto result = service.getAlumniGroupDtoById(1);
        assertEquals(dtoBack, result);
    }

    @Test
    void getDtoById_notFound_throwsException() {
        when(groupRepository.findByIdOptional(1L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> service.getAlumniGroupDtoById(1));
    }
}
