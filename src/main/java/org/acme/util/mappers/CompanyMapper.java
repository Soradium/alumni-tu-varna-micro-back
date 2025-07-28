package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.acme.dto.CompanyDto;
import org.acme.entites.Alumni;
import org.acme.entites.CompanyRecords;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.AlumniRepository;

@ApplicationScoped
public class CompanyMapper {
    @Inject
    AlumniRepository alumniRepository;

    public CompanyDto toDto(CompanyRecords record) {
        CompanyDto dto = new CompanyDto();
        dto.setId(record.getId());
        dto.setAlumniId(record.getAlumni().getId());
        dto.setEnrollmentDate(record.getEnrollmentDate());
        dto.setDischargeDate(record.getDischargeDate());
        dto.setPositionName(record.getPosition());
        dto.setCompanyName(record.getCompany());
        return dto;
    }

    public CompanyRecords toEntity(CompanyDto dto) {
        CompanyRecords record = new CompanyRecords();
        record.setEnrollmentDate(dto.getEnrollmentDate());
        record.setDischargeDate(dto.getDischargeDate());
        record.setPosition(dto.getPositionName());
        record.setCompany(dto.getCompanyName());

        Alumni alumni = alumniRepository.findByIdOptional(dto.getAlumniId())
                        .orElseThrow(() -> new ResourceNotFoundException("Alumni not found"));
        record.setAlumni(alumni);

        return record;
    }

    public CompanyRecords updateEntity(CompanyDto dto, CompanyRecords record) {
        if(dto.getDischargeDate() != null) {
            record.setDischargeDate(dto.getDischargeDate());
        }
        if(dto.getPositionName() != null) {
            record.setPosition(dto.getPositionName());
        }
        return record;
    }
}
