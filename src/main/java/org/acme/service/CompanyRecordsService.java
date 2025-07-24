package org.acme.service;

import org.acme.dto.CompanyDto;
import org.acme.entites.CompanyRecords;
import org.acme.exceptions.IncorrectAlumnusNumberException;

import java.util.List;

public interface CompanyRecordsService {
    List<CompanyDto> getCompanyRecordByAlumniId(int alumniId) throws Exception;
    List<CompanyDto> updateCompanyRecordsByAlumniId(int alumniId)throws Exception;
    CompanyDto createCompanyRecord(CompanyDto companyDto);
    CompanyDto updateCompanyRecord(Long id,CompanyDto companyDto) throws Exception;
    void mergeCompanyRecords(int alumniId,  List<CompanyRecords> dbRecords,
                             List<CompanyDto> apiRecords) throws Exception;


}
