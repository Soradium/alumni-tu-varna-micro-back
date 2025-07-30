package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.acme.dto.CompanyDto;
import org.acme.entites.Alumni;
import org.acme.entites.CompanyRecord;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.AlumniRepository;

@ApplicationScoped
public class CompanyMapper {
    @Inject
    AlumniRepository alumniRepository;

    public CompanyDto toDto(CompanyRecord record) {
        CompanyDto dto = new CompanyDto();
        dto.setId(record.getId());
        dto.setAlumniId(record.getAlumni().getFacultyNumber());
        dto.setEnrollmentDate(record.getEnrollmentDate());
        dto.setDischargeDate(record.getDischargeDate());
        dto.setPositionName(record.getPosition());
        dto.setCompanyName(record.getCompany());
        return dto;
    }

    public CompanyRecord toEntity(CompanyDto dto, Alumni alumni) {
        CompanyRecord record = new CompanyRecord();
        record.setEnrollmentDate(dto.getEnrollmentDate());
        record.setDischargeDate(dto.getDischargeDate());
        record.setPosition(dto.getPositionName());
        record.setCompany(dto.getCompanyName());
        record.setAlumni(alumni);
        return record;
    }

    public CompanyRecord updateEntity(CompanyDto dto, CompanyRecord record) {
        if(dto.getDischargeDate() != null) {
            record.setDischargeDate(dto.getDischargeDate());
        }
        if(dto.getPositionName() != null) {
            record.setPosition(dto.getPositionName());
        }
        return record;
    }
}
