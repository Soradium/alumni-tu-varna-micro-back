package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.avro.front.AlumniGroupMembershipFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;
import org.acme.repository.AlumniGroupRepository;
import org.acme.repository.AlumniGroupsMembershipRepository;
import org.acme.repository.AlumniRepository;
import org.acme.service.implementations.AlumniGroupMembershipServiceImpl;
import org.acme.util.mappers.AlumniGroupMembershipMapper;
import org.acme.util.mappers.AlumniMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.LockModeType;

public class AlumniGroupMembershipServiceTest {

    @Mock
    private AlumniGroupsMembershipRepository membershipRepository;
    @Mock
    private AlumniRepository alumniRepository;
    @Mock
    private AlumniGroupRepository groupRepository;
    @Mock
    private AlumniGroupMembershipMapper groupMembershipMapper;
    @Mock
    private AlumniMapper alumniMapper;

    @InjectMocks
    private AlumniGroupMembershipServiceImpl service;

    private AlumniGroupsMembership membership;
    private AlumniGroupMembershipFrontDto frontDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        membership = new AlumniGroupsMembership();
        membership.setId(1);
        membership.setAverageScore(4.0);

        frontDto = new AlumniGroupMembershipFrontDto();
        frontDto.setId(1);
        frontDto.setAverageScore(3.5);
        frontDto.setFacultyNumber(101);
        frontDto.setGroupNumber(202);
    }

    // -------- createAlumniGroupsMembership(FrontDto) --------

    @Test
    void createFromFrontDto_nullDto_throwsException() {
        Exception ex = assertThrows(Exception.class, () -> service.createAlumniGroupsMembership((AlumniGroupMembershipFrontDto) null));
        assertEquals("Membership DTO is null.", ex.getMessage());
    }

    @Test
    void createFromFrontDto_groupNotFound_throwsException() {
        when(groupMembershipMapper.toEntityFront(frontDto)).thenReturn(membership);
        when(groupRepository.findByIdOptional(202L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> service.createAlumniGroupsMembership(frontDto));
        assertEquals("AlumniGroup not found.", ex.getMessage());
    }

    @Test
    void createFromFrontDto_alumniNotFound_throwsException() {
        when(groupMembershipMapper.toEntityFront(frontDto)).thenReturn(membership);
        when(groupRepository.findByIdOptional(202L)).thenReturn(Optional.of(new AlumniGroup()));
        when(alumniRepository.findByIdOptional(101L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> service.createAlumniGroupsMembership(frontDto));
        assertEquals("Alumni with faculty number 101 was not found", ex.getMessage());
    }

    @Test
    void createFromFrontDto_success_persistsAndReturns() throws Exception {
        when(groupMembershipMapper.toEntityFront(frontDto)).thenReturn(membership);
        when(groupRepository.findByIdOptional(202L)).thenReturn(Optional.of(new AlumniGroup()));
        when(alumniRepository.findByIdOptional(101L)).thenReturn(Optional.of(new Alumni()));

        AlumniGroupsMembership result = service.createAlumniGroupsMembership(frontDto);

        verify(membershipRepository).persist(membership);
        assertSame(membership, result);
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

    @Test
    void getAllByFacultyNumber_returnsMappedDtos() throws Exception {
        when(membershipRepository.findAllByFacultyNumber(101)).thenReturn(Collections.singletonList(membership));
        when(groupMembershipMapper.toBackDtos(anyList())).thenReturn(Arrays.asList(new AlumniGroupMembershipDto()));

        List<AlumniGroupMembershipDto> result = service.getAllAlumniGroupMembershipsByFacultyNumber(101);
        assertEquals(1, result.size());
    }

    @Test
    void getAllByGroup_returnsMappedDtos() throws Exception {
        when(membershipRepository.findAllByGroupId(202)).thenReturn(Collections.singletonList(membership));
        when(groupMembershipMapper.toBackDtos(anyList())).thenReturn(Arrays.asList(new AlumniGroupMembershipDto()));

        List<AlumniGroupMembershipDto> result = service.getAllAlumniGroupMembershipsByGroup(202);
        assertEquals(1, result.size());
    }

    //TODO i'll fix the full implementation stubs later
    @Test
    void getAllDtos_returnsMappedDtos() throws Exception {
        when(membershipRepository.findAll()).thenReturn(new PanacheQuery<AlumniGroupsMembership>() {
            @Override public List<AlumniGroupsMembership> list() { return Collections.singletonList(membership); 
            }
            @Override
            public <T> PanacheQuery<T> project(Class<T> type) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'project'");
            }

            @Override
            public <T extends AlumniGroupsMembership> PanacheQuery<T> page(Page page) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'page'");
            }

            @Override
            public <T extends AlumniGroupsMembership> PanacheQuery<T> page(int pageIndex, int pageSize) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'page'");
            }

            @Override
            public <T extends AlumniGroupsMembership> PanacheQuery<T> nextPage() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'nextPage'");
            }

            @Override
            public <T extends AlumniGroupsMembership> PanacheQuery<T> previousPage() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'previousPage'");
            }

            @Override
            public <T extends AlumniGroupsMembership> PanacheQuery<T> firstPage() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'firstPage'");
            }

            @Override
            public <T extends AlumniGroupsMembership> PanacheQuery<T> lastPage() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'lastPage'");
            }

            @Override
            public boolean hasNextPage() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'hasNextPage'");
            }

            @Override
            public boolean hasPreviousPage() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'hasPreviousPage'");
            }

            @Override
            public int pageCount() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'pageCount'");
            }

            @Override
            public Page page() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'page'");
            }

            @Override
            public <T extends AlumniGroupsMembership> PanacheQuery<T> range(int startIndex, int lastIndex) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'range'");
            }

            @Override
            public <T extends AlumniGroupsMembership> PanacheQuery<T> withLock(LockModeType lockModeType) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'withLock'");
            }

            @Override
            public <T extends AlumniGroupsMembership> PanacheQuery<T> withHint(String hintName, Object value) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'withHint'");
            }

            @Override
            public <T extends AlumniGroupsMembership> PanacheQuery<T> filter(String filterName, Parameters parameters) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'filter'");
            }

            @Override
            public <T extends AlumniGroupsMembership> PanacheQuery<T> filter(String filterName,
                    Map<String, Object> parameters) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'filter'");
            }

            @Override
            public <T extends AlumniGroupsMembership> PanacheQuery<T> filter(String filterName) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'filter'");
            }

            @Override
            public long count() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'count'");
            }

            @Override
            public <T extends AlumniGroupsMembership> Stream<T> stream() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'stream'");
            }

            @Override
            public <T extends AlumniGroupsMembership> T firstResult() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'firstResult'");
            }

            @Override
            public <T extends AlumniGroupsMembership> Optional<T> firstResultOptional() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'firstResultOptional'");
            }

            @Override
            public <T extends AlumniGroupsMembership> T singleResult() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'singleResult'");
            }

            @Override
            public <T extends AlumniGroupsMembership> Optional<T> singleResultOptional() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'singleResultOptional'");
            }
        });
        when(groupMembershipMapper.toBackDtos(anyList())).thenReturn(Arrays.asList(new AlumniGroupMembershipDto()));

        List<AlumniGroupMembershipDto> result = service.getAllAlumniGroupMembershipsDto();
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

    // -------- getByName --------

    @Test
    void getByName_alwaysThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class, () -> service.getAlumniGroupsMembershipByName("test"));
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
