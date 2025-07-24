package org.acme.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.acme.api.CompanyGetterApi;
import org.acme.dto.CompanyDto;
import org.acme.entites.CompanyRecords;
import org.acme.exceptions.IncorrectAlumnusNumberException;
import org.acme.repository.CompanyRecordsRepository;
import org.acme.service.CompanyRecordsService;

import java.util.*;


public class CompanyRecordsServiceImpl implements CompanyRecordsService {

    private final CompanyRecordsRepository companyRecordsRepository;

    private final CompanyGetterApi companyGetterApi;

    public CompanyRecordsServiceImpl(CompanyRecordsRepository companyRecordsRepository,
                                     CompanyGetterApi companyGetterApi) {
        this.companyRecordsRepository = companyRecordsRepository;
        this.companyGetterApi = companyGetterApi;
    }

    @Override
    public List<CompanyDto> getCompanyRecordByAlumniId(int alumniId) throws Exception {
        // "-": incorrect id, db access failed
        // "+": list of records, empty list
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
        List<CompanyRecords> dbRecords = companyRecordsRepository.findByAlumniId(alumniId);
        //TODO: Map CompanyRecords to CompanyDto using a mapper
        return Collections.emptyList();
    }

    @Override
    public List<CompanyDto> updateCompanyRecordsByAlumniId(int alumniId) throws Exception {
        // "-": incorrect id, db access failed
        // "+": return if empty lists, else mergeRecords
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");

        List<CompanyRecords> dbRecords = companyRecordsRepository.findByAlumniId(alumniId);
        List<CompanyDto> apiRecords = companyGetterApi.getCompaniesPerAlumni(alumniId);

        if(dbRecords.isEmpty() || apiRecords.isEmpty())
            return Collections.emptyList();

        mergeCompanyRecords(alumniId, dbRecords, apiRecords);

        return Collections.emptyList();
    }

    @Override
    @Transactional
    public CompanyDto createCompanyRecord(CompanyDto companyDto){
        //"-": db access failed
        //"+": returns saved dto
        //TODO: Map CompanyDto to CompanyRecords
        //TODO: Persist to DB
        //TODO: Return saved DTO
        //companyRecordsRepository.persist(record);
        return new CompanyDto();
    }

    @Override
    @Transactional
    public CompanyDto updateCompanyRecord(Long id, CompanyDto dto) throws Exception {
        //"-": db access failed, no record found
        //"+": returns saved dto
        CompanyRecords record = companyRecordsRepository.findByIdOptional(id).orElseThrow(
                () -> new NotFoundException("Record " + id + " not found"));
        //TODO: Map CompanyDto to CompanyRecords, persist
        //companyRecordsRepository.persist(record);
        //TODO: return updated DTO
        return new CompanyDto();
    }

    @Override
    public void mergeCompanyRecords(int alumniId, List<CompanyRecords> dbRecords, List<CompanyDto> apiRecords) throws Exception {
        //"-": nothing to insert/update
        //"+": returns saved dto, calling create() and update()
        //TODO: compare existing and new records, insert new, update existing
//        Map<String, CompanyRecords> oldRecordsMap = dbRecords.stream()
//                .collect(Collectors.toMap(
//                        r -> r.getAlumni().getId() + "::" +
//                                r.getEnrollmentDate() + "::" +
//                                r.getCompany().toLowerCase().trim(),
//                        Function.identity()
//                ));
//
//        Map<String, CompanyDto> newRecordsMap = apiRecords.stream()
//                .collect(Collectors.toMap(
//                        r -> r.getAlumniId() + "::" +
//                                r.getEnrollmentDate() + "::" +
//                                r.getcompanyName().toLowerCase().trim(),
//                        Function.identity()
//                ));

        Map<String, CompanyRecords> oldRecordsMap = new HashMap<>();
        Map<String, CompanyDto> newRecordsMap = new HashMap<>();

        Set<String> existingKeys = oldRecordsMap.keySet();
        Set<String> freshKeys = newRecordsMap.keySet();

        Set<String> keysToInsert = new HashSet<>(freshKeys);
        keysToInsert.removeAll(existingKeys);

        Set<String> keysToUpdate = new HashSet<>(freshKeys);
        keysToUpdate.retainAll(existingKeys);

        for (String key : keysToInsert) {
            CompanyDto dto = newRecordsMap.get(key);
            createCompanyRecord(dto);
        }

        for (String key : keysToUpdate) {
            Long recordId = oldRecordsMap.get(key).getId();
            CompanyDto dto = newRecordsMap.get(key);
            updateCompanyRecord(recordId, dto);
        }

    }


}
