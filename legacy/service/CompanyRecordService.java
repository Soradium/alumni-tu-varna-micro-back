package org.acme.service;

import org.acme.avro.CompanyRecordDto;
import org.acme.entites.CompanyRecord;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface CompanyRecordService {
    List<CompanyRecordDto> getCompanyRecordByAlumniId(long alumniId) throws Exception;
    List<CompanyRecordDto> updateCompanyRecordsByAlumniId(long alumniId)throws Exception;
    CompanyRecordDto createCompanyRecord(CompanyRecordDto companyDto);
    List<CompanyRecordDto> createCompanyRecord(long id, List<CompanyRecordDto> companyRecordDtoList);
    CompanyRecordDto updateCompanyRecord(long id,CompanyRecordDto companyRecordDto) throws Exception;
    List<CompanyRecordDto> updateCompanyRecord(List<Pair<CompanyRecord, CompanyRecordDto>> updates);
    List<CompanyRecordDto> mergeCompanyRecords(
        long alumniId,  
        List<CompanyRecord> dbRecords,
        List<CompanyRecordDto> apiRecords) throws Exception;


}
