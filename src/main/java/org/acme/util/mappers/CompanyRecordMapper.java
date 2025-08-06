package org.acme.util.mappers;

import org.acme.avro.back.CompanyRecordDto;
import org.acme.entites.CompanyRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Mapper(componentModel = "cdi", uses = {
    SimpleAlumniMapper.class,
    DateMappingUtils.class,
    AlumniGroupMapper.class,
    AlumniGroupMembershipMapper.class
})
@ApplicationScoped
public abstract class CompanyRecordMapper {

    private final AlumniMapper alumniMapper;
    
    @Inject
    public CompanyRecordMapper(AlumniMapper alumniMapper) {
        this.alumniMapper = alumniMapper;
    }

    @Mapping(source = "enrollmentDate", target = "enrollmentDate")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapTimestampToInstant")
    public abstract CompanyRecordDto toBackDto(CompanyRecord record);

    @Named("toCompanyRecordEntity")
    public CompanyRecord toEntity(CompanyRecordDto dto) {
        if (dto == null || dto.getAlumni() == null ) return null;

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


}
