package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.acme.api.CompanyGetterApi;
import org.acme.avro.back.AlumniDto;
import org.acme.avro.back.CompanyRecordDto;
import org.acme.exceptions.IncorrectAlumnusNumberException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CompanyGetterTest {

    CompanyGetterApi api = Mockito.mock(CompanyGetterApi.class);

    @Test
    public void testCompaniesExistForValidAlumnusId() throws Exception {
        int alumnusId = 11;

        CompanyRecordDto company = new CompanyRecordDto();
        company.setId(1);
        company.setAlumni(new AlumniDto()); 
        company.setEnrollmentDate(LocalDate.now());
        company.setDischargeDate(LocalDate.now());
        company.setPosition("pos");
        company.setCompany("cp");
        company.setCreatedAt(Instant.now());

        List<CompanyRecordDto> companies = new ArrayList<>();
        companies.add(company);
        when(api.getCompaniesPerAlumni(alumnusId)).thenReturn(companies);

        List<CompanyRecordDto> result = api.getCompaniesPerAlumni(alumnusId);

        assertEquals(companies, result);
    }

    @Test
    public void testEmptyCompanyListForValidAlumnusId() throws Exception {
        int alumnusId = 11;
        List<CompanyRecordDto> companies = Collections.emptyList();
        when(api.getCompaniesPerAlumni(alumnusId)).thenReturn(companies);

        List<CompanyRecordDto> result = api.getCompaniesPerAlumni(alumnusId);

        assertEquals(companies, result);
    }

    @Test
    public void testExceptionThrownWhenStrategyIsNull() throws Exception {
        doThrow(new NullPointerException("No strategy set."))
            .when(api).getCompaniesPerAlumni(Mockito.anyInt());

        Throwable ex = assertThrows(NullPointerException.class,
            () -> api.getCompaniesPerAlumni(42));

        assertEquals("No strategy set.", ex.getMessage());
    }

    @Test
    public void testExceptionThrownWhenCompanyListIsNull() throws Exception {
        doThrow(new NullPointerException("No companies list loaded."))
            .when(api).getCompaniesPerAlumni(Mockito.anyInt());

        Throwable ex = assertThrows(NullPointerException.class,
            () -> api.getCompaniesPerAlumni(42));

        assertEquals("No companies list loaded.", ex.getMessage());
    }

    @Test
    public void testIncorrectAlumnusNumberThrowsException() throws Exception {
        when(api.getCompaniesPerAlumni(0))
            .thenThrow(new IncorrectAlumnusNumberException("The alumnus number is null."));

        Throwable ex = assertThrows(IncorrectAlumnusNumberException.class,
            () -> api.getCompaniesPerAlumni(0));

        assertEquals("The alumnus number is null.", ex.getMessage());
    }
}
