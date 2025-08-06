package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.acme.avro.CompanyRecordDto;
import org.acme.entites.Alumni;
import org.acme.entites.CompanyRecord;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.AlumniRepository;

@ApplicationScoped
public class CompanyRecordMapper {
    @Inject
    AlumniRepository alumniRepository;

    public CompanyRecordDto toDto(CompanyRecordDto record) {
        CompanyRecordDto dto = new CompanyRecordDto();
        dto.setId(record.getId());
        dto.setAlumniId(record.getAlumni().getFacultyNumber());
        dto.setEnrollmentDate(record.getEnrollmentDate());
        dto.setDischargeDate(record.getDischargeDate());
        dto.setPositionName(record.getPosition());
        dto.setCompanyName(record.getCompany());
        return dto;
    }

    public CompanyRecordDto toEntity(CompanyRecordDto dto, Alumni alumni) {
        CompanyRecord record = new CompanyRecord();
        record.setEnrollmentDate(dto.getEnrollmentDate());
        record.setDischargeDate(dto.getDischargeDate());
        record.setPosition(dto.getPositionName());
        record.setCompany(dto.getCompanyName());
        record.setAlumni(alumni);
        return record;
    }

    public CompanyRecordDto updateEntity(CompanyRecordDto dto, CompanyRecordDto record) {
        if(dto.getDischargeDate() != null) {
            record.setDischargeDate(dto.getDischargeDate());
        }
        if(dto.getPositionName() != null) {
            record.setPosition(dto.getPositionName());
        }
        return record;
    }
}
