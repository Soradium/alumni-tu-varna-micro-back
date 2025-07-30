package org.acme.services;

import jakarta.ws.rs.NotFoundException;
import org.acme.api.CompanyGetterApi;
import org.acme.dto.CompanyDto;
import org.acme.entites.Alumni;
import org.acme.entites.CompanyRecord;
import org.acme.exceptions.IncorrectAlumnusNumberException;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.AlumniRepository;
import org.acme.repository.CompanyRecordsRepository;
import org.acme.service.implementation.CompanyRecordServiceImpl;
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
    private CompanyRecordServiceImpl service;
    private CompanyRecordsRepository companyRecordsRepository;
    private AlumniRepository alumniRepository;
    private CompanyGetterApi companyGetterApi;

    @BeforeEach
    public void setUp() {
        companyRecordsRepository = Mockito.mock(CompanyRecordsRepository.class);
        companyGetterApi = Mockito.mock(CompanyGetterApi.class);
        alumniRepository = Mockito.mock(AlumniRepository.class);
        CompanyMapper companyMapper = new CompanyMapper();
        service = new CompanyRecordServiceImpl(companyRecordsRepository,
                alumniRepository, companyGetterApi,companyMapper);
    }

    //Tests for getCompanyRecordByAlumniId(int alumniId) method
    @Test
    public void test_getCompanyRecords_byValidId_returnsDtos() throws Exception {
        int validId = 6;
        Alumni mockAlumni1 = new Alumni();
        mockAlumni1.setFacultyNumber(validId);

        LocalDate enrollmentDate1 = LocalDate.of(2020, 1, 1);
        LocalDate enrollmentDate2 = LocalDate.of(2023, 1, 2);
        LocalDate dischargeDate1 = LocalDate.of(2022, 6, 3);
        CompanyRecord record1 = new CompanyRecord();
        record1.setId(3);
        record1.setAlumni(mockAlumni1);
        record1.setEnrollmentDate(enrollmentDate1);
        record1.setDischargeDate(dischargeDate1);
        record1.setPosition("QA");
        record1.setCompany("A");

        CompanyRecord record2 = new CompanyRecord();
        record2.setId(4);
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

    //nothing to update or insert
    @Test
    public void test_updateCompanyRecords_byValidId_callsMerge_returnsEmptyList() throws Exception {
        long validId = 6L;
        CompanyRecord rec = new CompanyRecord();
        rec.setEnrollmentDate(LocalDate.of(2020, 1, 1));
        rec.setCompany("A");
        rec.setPosition("QA");
        List<CompanyRecord> dbList = new ArrayList<>();
        dbList.add(rec);

        CompanyDto dto = new CompanyDto();
        dto.setEnrollmentDate(LocalDate.of(2020, 1, 1));
        dto.setCompanyName("A");
        dto.setPositionName("QA");
        List<CompanyDto> apiList = new ArrayList<>();
        apiList.add(dto);

        when(companyRecordsRepository.findByAlumniId(validId)).thenReturn(dbList);
        when(companyGetterApi.getCompaniesPerAlumni((int)validId)).thenReturn(apiList);

        CompanyRecordServiceImpl spyService = spy(service);
        List<CompanyDto> result = spyService.updateCompanyRecordsByAlumniId(validId);

        verify(spyService, times(1)).mergeCompanyRecords(validId, dbList, apiList);
        assertEquals(Collections.emptyList(), result);
    }

    // Tests for createCompanyRecord(CompanyDto dto)

    // Tests for updateCompanyRecord(Long id, CompanyDto dto)
    @Test
    public void test_updateCompanyRecord_byValidId_returnsDto(){
        long validId = 6L;
        CompanyRecord record = new CompanyRecord();
        Alumni alumni = new Alumni();
        alumni.setFacultyNumber(22221111);
        record.setAlumni(alumni);

        when(companyRecordsRepository.findByIdOptional(validId))
                .thenReturn(Optional.of(record));

        CompanyDto result = service.updateCompanyRecord(validId, new CompanyDto());

        assertNotNull(result);
    }

    @Test
    public void test_updateCompanyRecord_notFound_throwsException() {
        long validId = 6L;
        when(companyRecordsRepository.findByIdOptional(validId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateCompanyRecord(validId, new CompanyDto()));
    }

    @Test
    public void test_mergeCompanyRecords_callsCreateAndUpdate() throws Exception {
        long validId = 6L;
        CompanyRecord rec = new CompanyRecord();
        rec.setEnrollmentDate(LocalDate.of(2020, 1, 1));
        rec.setCompany("A");
        rec.setPosition("QA");
        List<CompanyRecord> dbList = List.of(rec);

        CompanyDto newDto = new CompanyDto();
        newDto.setEnrollmentDate(LocalDate.of(2024, 2, 1));
        newDto.setCompanyName("B");
        newDto.setPositionName("Developer");

        CompanyDto toUpdateDto = new CompanyDto();
        toUpdateDto.setEnrollmentDate(LocalDate.of(2020, 1, 1));
        toUpdateDto.setCompanyName("A");
        toUpdateDto.setPositionName("QA Lead");

        List<CompanyDto> apiList = List.of(newDto, toUpdateDto);

        when(companyRecordsRepository.findByAlumniId(validId)).thenReturn(dbList);
        when(companyGetterApi.getCompaniesPerAlumni((int)validId)).thenReturn(apiList);

        CompanyRecordServiceImpl spyService = spy(service);
        doReturn(List.of(newDto)).when(spyService).createCompanyRecord(validId, anyList());
        doReturn(List.of(toUpdateDto)).when(spyService).updateCompanyRecord(anyList());

        List<CompanyDto> result = spyService.updateCompanyRecordsByAlumniId(validId);

        List<CompanyDto> expected = new ArrayList<>();
        expected.add(newDto);
        expected.add(toUpdateDto);

        verify(spyService, times(1)).mergeCompanyRecords(validId, dbList, apiList);
        verify(spyService, times(1)).createCompanyRecord(validId, anyList());
        verify(spyService, times(1)).updateCompanyRecord(anyList());

        assertEquals(expected, result);
    }

    //TODO: tests for database access

}
