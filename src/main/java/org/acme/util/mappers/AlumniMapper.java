package org.acme.util.mappers;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;
import org.acme.entites.Degree;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
@Mapper(componentModel = "cdi", uses = {
    AlumniGroupMapper.class,
    AlumniGroupMembershipMapper.class,
    FacultyMapper.class,
    DegreeMapper.class, 
    DateMappingUtils.class
})
@ApplicationScoped
public abstract class AlumniMapper {    

    private final FacultyMapper facultyMapper;
    private final AlumniGroupMembershipMapper alumniGroupMembershipMapper;
    private final DegreeMapper degreeMapper;
    
    @Inject
    public AlumniMapper(FacultyMapper facultyMapper, AlumniGroupMembershipMapper alumniGroupMembershipMapper,
            DegreeMapper degreeMapper) {
        this.facultyMapper = facultyMapper;
        this.alumniGroupMembershipMapper = alumniGroupMembershipMapper;
        this.degreeMapper = degreeMapper;
    }

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
        alumni.setMemberships((ArrayList) alumniGroupMembershipMapper.toEntity(dto.getMemberships())); 
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
}