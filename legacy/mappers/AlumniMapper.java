package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.avro.AlumniDto;
import org.acme.dto.FacultyDto;
import org.acme.entites.*;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.DegreeRepository;
import org.acme.repository.FacultyRepository;
import org.acme.service.AlumniService;
import org.acme.service.DegreeService;
import org.acme.service.FacultyService;
import org.acme.service.implementation.MembershipServiceImpl;
import org.hibernate.type.descriptor.jdbc.InstantAsTimestampWithTimeZoneJdbcType;
import org.hibernate.type.descriptor.jdbc.InstantJdbcType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

@ApplicationScoped
public class AlumniMapper {
    private final DegreeMapper degreeMapper;
    private final AlumniGroupsMembershipMapper membershipMapper;
    private final FacultyService facultyService;
    private final AlumniService alumniService;
    private final DegreeService degreeService;
    

    // Alumni Dto reflects both, so just create 2 methods per each, they share the PK; Details do not need toDto 
    // methods specifically made for them, as full data is packed with AlumniDto

    public AlumniMapper(DegreeMapper degreeMapper, AlumniGroupsMembershipMapper membershipMapper,
            FacultyService facultyService, AlumniService alumniService, DegreeService degreeService) {
        this.degreeMapper = degreeMapper;
        this.membershipMapper = membershipMapper;
        this.facultyService = facultyService;
        this.alumniService = alumniService;
        this.degreeService = degreeService;
    }

    public AlumniDto alumniToDto(Alumni alumni, AlumniDetails details){
        if(alumni == null) {
            throw new NullPointerException("No alumni was entered.");
        }
        if(details == null) {
            throw new NullPointerException("No alumni details were entered.");
        }
        AlumniDto alumniDto = AlumniDto.newBuilder()
            .setBirthDate(details.getBirthDate())
            .setDegree(alumni.getDegree().getDegreeName())
            .setFacebookUrl(alumni.getFacebookUrl())
            .setLinkedinUrl(alumni.getLinkedInUrl())
            .setFacultyNumber(alumni.getFacultyNumber())
            .setFullName(details.getFullName())
            .setMemberships(
                alumni.getMemberships()
                .stream()
                .map(membershipMapper::toDto)
                .collect(Collectors
                .toCollection(ArrayList::new))
                )
            .setCreatedAt(details.getCreatedAt().toInstant())
            .setUpdatedAt(details.getUpdatedAt().toInstant())
            .setFaculty(details.getFaculty().getFacultyName())
            .build();

        return alumniDto;
    }

    public Alumni alumniToEntity(AlumniDto alumniDto) {
        if(alumniDto == null) {
            throw new ResourceNotFoundException(
                "NULL alumniDto was passed to the function."
                );
        }
        Alumni alumni = new Alumni();
        alumni.setFacultyNumber(alumniDto.getFacultyNumber());

        alumni.setMemberships();
        if(alumniDto.getFacebookUrl() != null){
            alumni.setFacebookUrl(alumniDto.getFacebookUrl());
        }
        if(alumniDto.getLinkedinUrl() != null){
            alumni.setLinkedInUrl(alumniDto.getLinkedinUrl());
        }
        alumni.setDegree(degree);

        return alumni;
    } 

    public AlumniDetails toDetailsEntity(AlumniDto dto, Faculty faculty){
        AlumniDetails entity = new AlumniDetails();
        entity.setFullName(dto.getFullName());
        entity.setBirthDate(dto.getBirthDate());
        entity.setCreatedAt(Timestamp.from(dto.getCreatedAt()));
        entity.setUpdatedAt(Timestamp.from(dto.getUpdatedAt()));
        entity.setFacultyNumber(dto.getFacultyNumber());
        entity.setFaculty(faculty);
        return entity;
    }

    public void updateDetails(AlumniDetailsDto dto, AlumniDetails entity, Faculty faculty){
        if(dto.getFullName() != null){
            entity.setFullName(dto.getFullName());
        }
        if(dto.getBirthDate() != null){
            entity.setBirthDate(dto.getBirthDate());
        }
        if(dto.getFaculty() != null) {
            entity.setFaculty(faculty);
        }
    }
}
