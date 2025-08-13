package org.acme.util.mappers;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;
import org.acme.entites.Degree;
import org.acme.entites.Faculty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA, uses = {
        AlumniGroupMembershipMapper.class,
        FacultyMapper.class,
        DegreeMapper.class,
        DateMappingUtils.class
})
@ApplicationScoped
public abstract class AlumniMapper {

    private FacultyMapper facultyMapper;
    private AlumniGroupMembershipMapper alumniGroupMembershipMapper;
    private DegreeMapper degreeMapper;
    private DateMappingUtils dateMappingUtils;

    @Mapping(target = "memberships", source = "alumni.memberships")
    @Mapping(target = "fullName", source = "alumniDetails.fullName")
    @Mapping(target = "birthDate", source = "alumniDetails.birthDate")
    @Mapping(target = "faculty", source = "alumniDetails.faculty")
    @Mapping(target = "createdAt", source = "alumniDetails.createdAt", qualifiedByName = "mapTimestampToInstant")
    @Mapping(target = "updatedAt", source = "alumniDetails.updatedAt", qualifiedByName = "mapTimestampToInstant")
    @Mapping(target = "facultyNumber", source = "alumni.facultyNumber")
    @Mapping(target = "facebookUrl", source = "alumni.facebookUrl")
    @Mapping(target = "linkedinUrl", source = "alumni.linkedInUrl")
    @Mapping(target = "degree", source = "alumni.degree")
    @Named(value = "toAlumniDtoBack")
    public abstract AlumniDto toDto(Alumni alumni, AlumniDetails alumniDetails);

    @Named("toAlumniEntity")
    public Alumni toAlumniEntity(AlumniDto dto) {
        Alumni alumni = new Alumni();
        alumni.setFacultyNumber(dto.getFacultyNumber());
        alumni.setFacebookUrl(dto.getFacebookUrl());
        alumni.setLinkedInUrl(dto.getLinkedinUrl());
        alumni.setDegree(degreeMapper.toEntity(dto.getDegree()));
        alumni.setMemberships((ArrayList) alumniGroupMembershipMapper.toEntityList(dto.getMemberships()));
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
        alumni.setMemberships((ArrayList) alumniGroupMembershipMapper.toBasicEntity(dto.getMembershipIds()));
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

    @Inject
    public void setFacultyMapper(FacultyMapper facultyMapper) {
        this.facultyMapper = facultyMapper;
    }

    @Inject
    public void setAlumniGroupMembershipMapper(AlumniGroupMembershipMapper alumniGroupMembershipMapper) {
        this.alumniGroupMembershipMapper = alumniGroupMembershipMapper;
    }

    @Inject
    public void setDegreeMapper(DegreeMapper degreeMapper) {
        this.degreeMapper = degreeMapper;
    }

    @Inject
    public void setDateMappingUtils(DateMappingUtils dateMappingUtils) {
        this.dateMappingUtils = dateMappingUtils;
    }
}