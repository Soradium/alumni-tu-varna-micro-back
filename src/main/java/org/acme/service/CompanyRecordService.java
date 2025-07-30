package org.acme.service;

import org.acme.dto.CompanyDto;
import org.acme.entites.CompanyRecord;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface CompanyRecordService {
    List<CompanyDto> getCompanyRecordByAlumniId(long alumniId) throws Exception;
    List<CompanyDto> updateCompanyRecordsByAlumniId(long alumniId)throws Exception;
    CompanyDto createCompanyRecord(CompanyDto companyDto);
    List<CompanyDto> createCompanyRecord(long id, List<CompanyDto> companyDtoList);
    CompanyDto updateCompanyRecord(long id,CompanyDto companyDto) throws Exception;
    List<CompanyDto> updateCompanyRecord(List<Pair<CompanyRecord, CompanyDto>> updates);
    List<CompanyDto> mergeCompanyRecords(long alumniId,  List<CompanyRecord> dbRecords,
                             List<CompanyDto> apiRecords) throws Exception;


}
