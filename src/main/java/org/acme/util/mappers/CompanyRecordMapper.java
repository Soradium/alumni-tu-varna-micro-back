package org.acme.util.mappers;

import org.acme.avro.back.CompanyRecordDto;
import org.acme.entites.CompanyRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import jakarta.inject.Inject;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA, uses = {
        AlumniMapper.class,
        DateMappingUtils.class,

})
public abstract class CompanyRecordMapper {

    private AlumniMapper alumniMapper;
    private DateMappingUtils dateMappingUtils;

    @Mapping(source = "enrollmentDate", target = "enrollmentDate")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapTimestampToInstant")
    public abstract CompanyRecordDto toBackDto(CompanyRecord record);

    @Named("toCompanyRecordEntity")
    public CompanyRecord toEntity(CompanyRecordDto dto) {
        if (dto == null || dto.getAlumni() == null) return null;

        CompanyRecord entity = new CompanyRecord();

        entity.setId(dto.getId());
        entity.setEnrollmentDate(dto.getEnrollmentDate());
        entity.setDischargeDate(dto.getDischargeDate());
        entity.setPosition(dto.getPosition());
        entity.setCompany(dto.getCompany());
        entity.setCreatedAt(DateMappingUtils.mapInstantToTimestamp(dto.getCreatedAt())); // Assuming reverse of `mapTimestampToInstant`
        entity.setAlumni(alumniMapper.toAlumniEntity(dto.getAlumni()));

        return entity;
    }

    @Inject
    public void setAlumniMapper(AlumniMapper alumniMapper) {
        this.alumniMapper = alumniMapper;
    }

    @Inject
    public void setDateMappingUtils(DateMappingUtils dateMappingUtils) {
        this.dateMappingUtils = dateMappingUtils;
    }


}
