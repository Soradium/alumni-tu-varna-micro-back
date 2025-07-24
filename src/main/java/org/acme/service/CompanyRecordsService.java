package org.acme.service;

import org.acme.dto.CompanyDto;
import org.acme.entites.CompanyRecords;
import org.acme.exceptions.IncorrectAlumnusNumberException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface CompanyRecordsService {
    List<CompanyDto> getCompanyRecordByAlumniId(long alumniId) throws Exception;
    List<CompanyDto> updateCompanyRecordsByAlumniId(long alumniId)throws Exception;
    CompanyDto createCompanyRecord(CompanyDto companyDto);
    List<CompanyDto> createCompanyRecord(List<CompanyDto> companyDtoList);
    CompanyDto updateCompanyRecord(long id,CompanyDto companyDto) throws Exception;
    List<CompanyDto> updateCompanyRecord(List<Pair<CompanyRecords, CompanyDto>> updates);
    List<CompanyDto> mergeCompanyRecords(long alumniId,  List<CompanyRecords> dbRecords,
                             List<CompanyDto> apiRecords) throws Exception;


}
