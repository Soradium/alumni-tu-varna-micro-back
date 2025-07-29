package org.acme;

import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;

import org.acme.exceptions.IncorrectAlumnusNumberException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.acme.api.CompanyGetterApi;
import org.acme.dto.CompanyDto;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompanyGetterTest {

    CompanyGetterApi api = Mockito.mock(CompanyGetterApi.class);

    @Test
    public void test_companies_exist_for_valid_alumnus_id() throws Exception{
        int alumnId = 11;
        CompanyDto company = new CompanyDto(
            Long.valueOf(1),
            Long.valueOf(2),
            LocalDate.now(),
            LocalDate.now(),
            "pos",
            "cp"
        ); 
        List<CompanyDto> companies = new ArrayList<>();
        companies.add(company);
        when(api.getCompaniesPerAlumni(alumnId)).thenReturn(companies);

        List<CompanyDto> result = api.getCompaniesPerAlumni(alumnId);

        assertEquals(companies, result);
    }

    @Test
    public void test_empty_company_list_for_valid_alumnus_id() throws Exception{
        int alumnId = 11;
        List<CompanyDto> companies = Collections.emptyList();
        when(api.getCompaniesPerAlumni(alumnId)).thenReturn(companies);

        List<CompanyDto> result = api.getCompaniesPerAlumni(alumnId);

        assertEquals(companies, result);
    }

    @Test
    public void test_exception_thrown_when_strategy_is_null() throws Exception{
        doThrow(new NullPointerException("No strategy set."))
            .when(api).getCompaniesPerAlumni(Mockito.anyInt());

        Throwable ex = assertThrows(NullPointerException.class,
            () -> api.getCompaniesPerAlumni(42));

        assertEquals("No strategy set.", ex.getMessage());
    }

    @Test
    public void test_exception_thrown_when_company_list_is_null() throws Exception{
        doThrow(new NullPointerException("No companies list loaded."))
            .when(api).getCompaniesPerAlumni(Mockito.anyInt());

        Throwable ex = assertThrows(NullPointerException.class,
            () -> api.getCompaniesPerAlumni(42));

        assertEquals("No companies list loaded.", ex.getMessage());
    }

    @Test
    public void test_incorrect_alumnus_number_throws_exception() throws Exception{
        when(api.getCompaniesPerAlumni(0))
            .thenThrow(new IncorrectAlumnusNumberException("The alumnus number is null."));

        Throwable ex = assertThrows(IncorrectAlumnusNumberException.class,
            () -> api.getCompaniesPerAlumni(0));

        assertEquals("The alumnus number is null.", ex.getMessage());
    }

}
