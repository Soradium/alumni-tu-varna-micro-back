package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.acme.api.CompanyGetterApi;
import org.acme.dto.CompanyDto;
import org.acme.entites.Alumni;
import org.acme.entites.CompanyRecords;
import org.acme.exceptions.IncorrectAlumnusNumberException;
import org.acme.repository.CompanyRecordsRepository;
import org.acme.service.implementation.CompanyRecordsServiceImpl;
import org.acme.util.mappers.CompanyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class CompanyServiceTest {
    private CompanyRecordsServiceImpl service;
    private CompanyRecordsRepository companyRecordsRepository;
    private CompanyGetterApi companyGetterApi;

    @BeforeEach
    public void setUp() {
        companyRecordsRepository = Mockito.mock(CompanyRecordsRepository.class);
        companyGetterApi = Mockito.mock(CompanyGetterApi.class);
        CompanyMapper companyMapper = new CompanyMapper();
        service = new CompanyRecordsServiceImpl(companyRecordsRepository, companyGetterApi, companyMapper);
    }

    //Tests for getCompanyRecordByAlumniId(int alumniId) method
    @Test
    public void test_getCompanyRecords_byValidId_returnsDtos() throws Exception {
        long validId = 6L;
        Alumni mockAlumni1 = new Alumni();
        mockAlumni1.setId(validId);

        LocalDate enrollmentDate1 = LocalDate.of(2020, 1, 1);
        LocalDate enrollmentDate2 = LocalDate.of(2023, 1, 2);
        LocalDate dischargeDate1 = LocalDate.of(2022, 6, 3);
        CompanyRecords record1 = new CompanyRecords();
        record1.setId(3L);
        record1.setAlumni(mockAlumni1);
        record1.setEnrollmentDate(enrollmentDate1);
        record1.setDischargeDate(dischargeDate1);
        record1.setPosition("QA");
        record1.setCompany("A");

        CompanyRecords record2 = new CompanyRecords();
        record2.setId(4L);
        record2.setAlumni(mockAlumni1);
        record2.setEnrollmentDate(enrollmentDate2);
        record2.setPosition("Intern");
        record2.setCompany("B");

        when(companyRecordsRepository.findByAlumniId(validId))
                .thenReturn(List.of(record1, record2));

        List<CompanyDto> result = service.getCompanyRecordByAlumniId(validId);

        assertEquals(2, result.size());
        CompanyDto result1 = result.get(0);
        assertEquals(validId, result1.getAlumniId());
        assertEquals(enrollmentDate1, result1.getEnrollmentDate());
        assertEquals("A", result1.getCompanyName());
        assertEquals("QA", result1.getPositionName());

        CompanyDto result2 = result.get(1);
        assertEquals(validId, result2.getAlumniId());
        assertEquals(enrollmentDate2, result2.getEnrollmentDate());
        assertEquals("B", result2.getCompanyName());
        assertEquals("Intern", result2.getPositionName());
    }

    @Test
    public void test_getCompanyRecords_byValidId_noAlumniFound_returnsEmptyList() throws Exception {
        int validId = 6;
        when(companyRecordsRepository.findByAlumniId(validId))
                .thenReturn(Collections.emptyList());

        List<CompanyDto> result = service.getCompanyRecordByAlumniId(validId);

        assertTrue(result.isEmpty());
    }

    @Test
    public void test_getCompanyRecord_byInvalidId_throwsException(){
        Throwable t = assertThrows(IncorrectAlumnusNumberException.class,
                () -> service.getCompanyRecordByAlumniId(0));

        assertEquals("Alumnus id value must be positive", t.getMessage());
    }

    @Test
    public void test_dbFailurePropagates()  {
        int validId = 6;
        when(companyRecordsRepository.findByAlumniId(validId))
                .thenThrow(new RuntimeException("DB is down"));
        Throwable t = assertThrows(RuntimeException.class, () -> service.getCompanyRecordByAlumniId(validId));
        assertEquals("DB is down", t.getMessage());
    }

    //Tests for updateCompanyRecordsByAlumniId(int alumniId)

    @Test
    public void test_updateCompanyRecords_byInvalidId_throwsException(){
        Throwable t = assertThrows(IncorrectAlumnusNumberException.class,
                () -> service.getCompanyRecordByAlumniId(0));

        assertEquals("Alumnus id value must be positive", t.getMessage());
    }

    @Test
    public void test_updateCompanyRecords_byValidId_emptyRecords() throws Exception {
        int validId = 6;
        when(companyRecordsRepository.findByAlumniId(validId)).thenReturn(Collections.emptyList());
        when(companyGetterApi.getCompaniesPerAlumni(validId)).thenReturn(Collections.emptyList());

        List<CompanyDto> result = service.updateCompanyRecordsByAlumniId(validId);

        assertTrue(result.isEmpty());
    }

    @Test
    public void test_updateCompanyRecords_byValidId_callsMerge() throws Exception {
        int validId = 6;
        List<CompanyRecords> dbList = new ArrayList<>();
        dbList.add(new CompanyRecords());

        List<CompanyDto> apiList = new ArrayList<>();
        apiList.add(new CompanyDto());

        when(companyRecordsRepository.findByAlumniId(validId)).thenReturn(dbList);
        when(companyGetterApi.getCompaniesPerAlumni(validId)).thenReturn(apiList);

        CompanyRecordsServiceImpl spyService = spy(service);
        doNothing().when(spyService).mergeCompanyRecords(eq(validId), anyList(), anyList());

        spyService.updateCompanyRecordsByAlumniId(validId);

        verify(spyService, times(1)).mergeCompanyRecords(eq(validId), eq(dbList), eq(apiList));
    }

    // Tests for createCompanyRecord(CompanyDto dto)

    // Tests for updateCompanyRecord(Long id, CompanyDto dto)
    @Test
    public void test_updateCompanyRecord_byValidId_returnsDto() throws Exception {
        long validId = 6L;
        CompanyRecords record = new CompanyRecords();

        when(companyRecordsRepository.findByIdOptional(validId))
                .thenReturn(Optional.of(record));

        CompanyDto result = service.updateCompanyRecord(validId, new CompanyDto());

        assertNotNull(result);
    }

    @Test
    public void test_updateCompanyRecord_notFound_throwsException() {
        long validId = 6L;
        when(companyRecordsRepository.findByIdOptional(validId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.updateCompanyRecord(validId, new CompanyDto()));
    }

    @Test
    public void test_mergeCompanyRecords_callsCreateAndUpdate() throws Exception {
    }

    @Test
    public void test_mergeCompanyRecords_withNoDifferences_doesNothing() throws Exception {}
    //TODO: tests for database access failure(?)

}
