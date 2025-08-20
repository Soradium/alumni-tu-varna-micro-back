package org.acme.util.mappers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.acme.avro.ambiguous.DegreeDto;
import org.acme.avro.ambiguous.FacultyDto;
import org.acme.avro.back.AlumniDto;
import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;
import org.acme.entites.AlumniGroupsMembership;
import org.acme.entites.Degree;
import org.acme.entites.Faculty;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import jakarta.inject.Inject;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA, uses = {
        AlumniGroupCommonMapper.class,
        FacultyMapper.class,
        DegreeMapper.class,
        DateMappingUtils.class,
        SpecialityMapper.class
})
public abstract class AlumniMapper {

    private FacultyMapper facultyMapper;
    private AlumniGroupCommonMapper commonMapper;
    private DegreeMapper degreeMapper;
    private DateMappingUtils dateMappingUtils;
    private SpecialityMapper specialityMapper;

@Named("toAlumniDtoBack")
public AlumniDto toDto(Alumni alumni, AlumniDetails alumniDetails) {
    AlumniDto dto = new AlumniDto();

    // simple fields
    dto.setFacultyNumber(alumni.getFacultyNumber());
    dto.setFullName(alumniDetails.getFullName());
    dto.setBirthDate(alumniDetails.getBirthDate());
    dto.setFacebookUrl(alumni.getFacebookUrl());
    dto.setLinkedinUrl(alumni.getLinkedInUrl());
    dto.setDegree(degreeMapper.toDto(alumni.getDegree()));
    dto.setFaculty(facultyMapper.toDto(alumniDetails.getFaculty()));

    // builder fields (Avro)
    if (alumniDetails.getFaculty() != null) {
        dto.setFaculty(
            FacultyDto.newBuilder()
                .setId(alumniDetails.getFaculty().getId())
                .setFacultyName(alumniDetails.getFaculty().getFacultyName())
                .build()
        );
    }

    if (alumni.getDegree() != null) {
        dto.setDegree(
            DegreeDto.newBuilder()
                .setId(alumni.getDegree().getId())
                .setDegreeName(alumni.getDegree().getDegreeName())
                .build()
        );
    }

    // memberships mapping
    dto.setMemberships(
        alumni.getMemberships().stream()
              .map(this::toBackDtoMembership) // map each membership to AlumniGroupMembershipDto
              .collect(Collectors.toList())
    );

    return dto;
}


    @Named("toAlumniEntity")
    public Alumni toAlumniEntity(AlumniDto dto) {
        Alumni alumni = new Alumni();
        alumni.setFacultyNumber(dto.getFacultyNumber());
        alumni.setFacebookUrl(dto.getFacebookUrl());
        alumni.setLinkedInUrl(dto.getLinkedinUrl());
        alumni.setDegree(degreeMapper.toEntity(dto.getDegree()));
        alumni.setMemberships((ArrayList) commonMapper.toEntityList(dto.getMemberships()));
        return alumni;
    }

    @Named("toAlumniEntityFront")
    public Alumni toAlumniEntityFront(AlumniFrontDto dto) {
        Alumni alumni = new Alumni();
        Degree degree = new Degree();
        degree.setDegree(dto.getDegree());
        alumni.setFacultyNumber(dto.getFacultyNumber());
        alumni.setFacebookUrl(dto.getFacebookUrl());
        alumni.setLinkedInUrl(dto.getLinkedinUrl());
        alumni.setDegree(degree);
        alumni.setMemberships((ArrayList) commonMapper.toBasicEntity(dto.getMembershipIds()));
        // will require enrichment
        return alumni;
    }

    @Named("toAlumniDetailsEntity")
    public AlumniDetails toAlumniDetailsEntity(AlumniDto dto) {
        AlumniDetails alumniDetails = new AlumniDetails();

        alumniDetails.setFacultyNumber(dto.getFacultyNumber());
        alumniDetails.setFullName(dto.getFullName());
        alumniDetails.setBirthDate(dto.getBirthDate());

        alumniDetails.setFaculty(facultyMapper.toEntity(dto.getFaculty()));

        if (dto.getCreatedAt() != null) {
            alumniDetails.setCreatedAt(Timestamp.from(dto.getCreatedAt()));
        }

        if (dto.getUpdatedAt() != null) {
            alumniDetails.setUpdatedAt(Timestamp.from(dto.getUpdatedAt()));
        }

        return alumniDetails;
    }

    @Named("toAlumniDetailsEntityFront")
    public AlumniDetails toAlumniDetailsEntityFront(AlumniFrontDto dto) {
        AlumniDetails alumniDetails = new AlumniDetails();

        alumniDetails.setFacultyNumber(dto.getFacultyNumber());
        alumniDetails.setFullName(dto.getFullName());
        alumniDetails.setBirthDate(dto.getBirthDate());
        Faculty f = new Faculty();
        f.setFacultyName(dto.getFaculty());
        alumniDetails.setFaculty(f);

        if (dto.getCreatedAt() != null) {
            alumniDetails.setCreatedAt(Timestamp.from(dto.getCreatedAt()));
        }
        
        if (dto.getUpdatedAt() != null) {
            alumniDetails.setUpdatedAt(Timestamp.from(dto.getUpdatedAt()));
        }
        
        return alumniDetails;
    }
    
        private AlumniGroupMembershipDto toBackDtoMembership(AlumniGroupsMembership entity) {
            AlumniGroupMembershipDto dto = new AlumniGroupMembershipDto();
    
            dto.setFacultyNumber(entity.getAlumni().getFacultyNumber());
    
            if (entity.getGroup() != null) {
                dto.setGroup(
                    AlumniGroupBackDto.newBuilder()
                        .setId(entity.getGroup().getId())
                        .setFaculty(facultyMapper.toDto(entity.getGroup().getFaculty()))
                        .setGroupNumber(entity.getGroup().getGroupNumber())
                        .setSpeciality(specialityMapper.toDto(entity.getGroup().getSpeciality()))
                        .setMembershipIds(commonMapper.extractMembershipIds(entity.getGroup().getMemberships()))
                        .setGraduationYear(entity.getGroup().getGraduationYear())
                        .build()
                );
            }
    
            return dto;
        }
    
    @Inject
    public void setFacultyMapper(FacultyMapper facultyMapper) {
        this.facultyMapper = facultyMapper;
    }

    @Inject
    public void setAlumniGroupMembershipMapper(AlumniGroupCommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    @Inject
    public void setDegreeMapper(DegreeMapper degreeMapper) {
        this.degreeMapper = degreeMapper;
    }

    @Inject
    public void setDateMappingUtils(DateMappingUtils dateMappingUtils) {
        this.dateMappingUtils = dateMappingUtils;
    }

    @Inject
    public FacultyMapper getFacultyMapper() {
        return facultyMapper;
    }

    @Inject
    public AlumniGroupCommonMapper getCommonMapper() {
        return commonMapper;
    }

    @Inject
    public void setCommonMapper(AlumniGroupCommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    @Inject
    public DegreeMapper getDegreeMapper() {
        return degreeMapper;
    }

    @Inject
    public DateMappingUtils getDateMappingUtils() {
        return dateMappingUtils;
    }

    @Inject
    public SpecialityMapper getSpecialityMapper() {
        return specialityMapper;
    }

    @Inject
    public void setSpecialityMapper(SpecialityMapper specialityMapper) {
        this.specialityMapper = specialityMapper;
    }

}