package org.acme.service.implementation;

import jakarta.transaction.Transactional;
import org.acme.api.CompanyGetterApi;
import org.acme.dto.CompanyDto;
import org.acme.entites.CompanyRecords;
import org.acme.exceptions.IncorrectAlumnusNumberException;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.CompanyRecordsRepository;
import org.acme.service.CompanyRecordsService;
import org.acme.util.mappers.CompanyMapper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class CompanyRecordsServiceImpl implements CompanyRecordsService {

    private final CompanyRecordsRepository companyRecordsRepository;
    private final CompanyGetterApi companyGetterApi;
    private final CompanyMapper companyMapper;

    public CompanyRecordsServiceImpl(CompanyRecordsRepository companyRecordsRepository,
                                     CompanyGetterApi companyGetterApi, CompanyMapper companyMapper) {
        this.companyRecordsRepository = companyRecordsRepository;
        this.companyGetterApi = companyGetterApi;
        this.companyMapper = companyMapper;
    }

    @Override
    public List<CompanyDto> getCompanyRecordByAlumniId(long alumniId) throws Exception {
        // "-": incorrect id, db access failed
        // "+": list of records, empty list
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
        List<CompanyRecords> dbRecords = companyRecordsRepository.findByAlumniId(alumniId);
        if(dbRecords.isEmpty())
            return Collections.emptyList();
        return dbRecords.stream()
                .map(companyMapper::toDto).toList();
    }

    @Override
    public List<CompanyDto> updateCompanyRecordsByAlumniId(long alumniId) throws Exception {
        // "-": incorrect id, db access failed
        // "+": return if empty lists, else mergeRecords
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");

        List<CompanyRecords> dbRecords = companyRecordsRepository.findByAlumniId(alumniId);
        List<CompanyDto> apiRecords = companyGetterApi.getCompaniesPerAlumni(alumniId);

        if(dbRecords.isEmpty() || apiRecords.isEmpty())
            return Collections.emptyList();

        return mergeCompanyRecords(alumniId, dbRecords, apiRecords);
    }

    @Override
    @Transactional
    public CompanyDto createCompanyRecord(CompanyDto companyDto){
        //"-": db access failed
        //"+": returns saved dto
        CompanyRecords record = companyMapper.toEntity(companyDto);
        companyRecordsRepository.persist(record);
        return companyMapper.toDto(record);
    }

    @Override
    @Transactional
    public List<CompanyDto> createCompanyRecord(List<CompanyDto> companyDtoList){
        //"-": db access failed, empty entry list
        //"+": returns saved dtoList
        if (companyDtoList == null || companyDtoList.isEmpty()) {
            return Collections.emptyList();
        }
        List<CompanyRecords> recordList = companyDtoList.stream()
                .map(companyMapper::toEntity).toList();
        companyRecordsRepository.persist(recordList);
        return recordList.stream().map(companyMapper::toDto).toList();
    }

    @Override
    @Transactional
    public CompanyDto updateCompanyRecord(long id, CompanyDto dto) {
        //"-": db access failed, no record found
        //"+": returns saved dto
        CompanyRecords record = companyRecordsRepository.findByIdOptional(id).orElseThrow(
                () -> new ResourceNotFoundException("Record " + id + " not found"));
        companyMapper.updateEntity(dto, record);
        companyRecordsRepository.persist(record);
        return companyMapper.toDto(record);
    }

    @Override
    @Transactional
    public List<CompanyDto> updateCompanyRecord(List<Pair<CompanyRecords, CompanyDto>> updates) {
        if (updates == null || updates.isEmpty()) {
            return Collections.emptyList();
        }

        List<CompanyRecords> updated = new ArrayList<>();
        List<CompanyDto> updatedDto = new ArrayList<>();

        for (Pair<CompanyRecords, CompanyDto> pair : updates) {
            CompanyRecords record = pair.getLeft();
            CompanyDto dto = pair.getRight();
            CompanyRecords updatedRecord = companyMapper.updateEntity(dto, record);
            updated.add(updatedRecord);
            updatedDto.add(dto);
        }
        companyRecordsRepository.persist(updated);
        return updatedDto;
    }

    @Override
    @Transactional
    public List<CompanyDto> mergeCompanyRecords(long alumniId, List<CompanyRecords> dbRecords, List<CompanyDto> apiRecords) throws Exception {
        //"-":
        //"+": calls create() and/or update() returns lists of records,if nothing to insert/update, then empty list.
        Map<Boolean, List<CompanyDto>> partitionByNullDateAndCompany =
                apiRecords.stream().collect(Collectors.partitioningBy(
                        dto -> dto.getEnrollmentDate() != null
                                && dto.getCompanyName() != null
                ));

        List<CompanyDto> filteredApiRecords = partitionByNullDateAndCompany.get(true);
        List<CompanyDto> corruptedRecords = partitionByNullDateAndCompany.get(false);
        //logging corrupted records? return them? or Kafka will filter null fields?

        Map<String, CompanyRecords> dbRecordsMap = dbRecords.stream()
                .collect(Collectors.toMap(
                        this::keyFromRecord,
                        Function.identity()
                ));

        List<CompanyDto> toInsert = new ArrayList<>();
        List<Pair<CompanyRecords, CompanyDto>> toUpdate = new ArrayList<>();

        for (CompanyDto dto : filteredApiRecords) {
            String key = keyFromDto(dto);
            if (dbRecordsMap.containsKey(key)) {
                if(dbRecordsMap.get(key).getDischargeDate() != dto.getDischargeDate()
                && !(dbRecordsMap.get(key).getPosition().equalsIgnoreCase(dto.getPositionName())))
                {
                CompanyRecords dbRecord = dbRecordsMap.get(key);
                toUpdate.add(Pair.of(dbRecord, dto));
                }
            } else {
                toInsert.add(dto);
            }
        }
        List<CompanyDto> inserted = createCompanyRecord(toInsert);
        List<CompanyDto> updated = updateCompanyRecord(toUpdate);

        List<CompanyDto> merged = new ArrayList<>();
        merged.addAll(inserted);
        merged.addAll(updated);
        return merged;
    }

    private String keyFromRecord(CompanyRecords record) {
        //check for null here instead of merge
        return record.getEnrollmentDate() + "::" + record.getCompany().toLowerCase().trim();
    }

    private String keyFromDto(CompanyDto dto) {
        //check for null here instead of merge
        return dto.getEnrollmentDate() + "::" + dto.getCompanyName().toLowerCase().trim();
    }
}
