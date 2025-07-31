package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.dto.AlumniDetailsDto;
import org.acme.dto.AlumniDto;
import org.acme.entites.*;
import org.acme.repository.DegreeRepository;
import org.acme.repository.FacultyRepository;

import java.util.ArrayList;
import java.util.stream.Collectors;

@ApplicationScoped
public class AlumniMapper {
    @Inject
    public DegreeMapper degreeMapper;
    @Inject
    public AlumniGroupsMembershipMapper membershipMapper;


    public AlumniDto toDto(Alumni alumni){
        AlumniDto alumniDto = new AlumniDto();
        alumniDto.setFacultyNumber(alumni.getFacultyNumber());
        alumniDto.setFacebookUrl(alumni.getFacebookUrl());
        alumniDto.setLinkedInUrl(alumni.getLinkedInUrl());
        alumniDto.setDegreeDto(degreeMapper.toDto(alumni.getDegree()));

        alumniDto.setMemberships(
                alumni.getMemberships().stream().map(membershipMapper::toDto)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
        return alumniDto;
    }
    public Alumni toEntity(AlumniDto alumniDto, Degree degree){
        Alumni alumni = new Alumni();
        if(alumniDto.getFacebookUrl() != null){
            alumni.setFacebookUrl(alumniDto.getFacebookUrl());
        }
        if(alumniDto.getLinkedInUrl() != null){
            alumni.setLinkedInUrl(alumniDto.getLinkedInUrl());
        }
        alumni.setDegree(degree);
        return alumni;
    }

    public void updateEntity(AlumniDto alumniDto, Alumni alumni, Degree degree){
        if(alumniDto.getFacebookUrl() != null){
            alumni.setFacebookUrl(alumniDto.getFacebookUrl());
        }
        if(alumniDto.getLinkedInUrl() != null){
            alumni.setLinkedInUrl(alumniDto.getLinkedInUrl());
        }
        if(alumniDto.getDegreeDto() != null){
            alumni.setDegree(degree);
        }
    }

    public AlumniDetailsDto toDetailsDto(AlumniDetails alumniDetails){
        AlumniDetailsDto dto = new AlumniDetailsDto();
        dto.setFacultyNumber(alumniDetails.getFacultyNumber());
        dto.setFullName(alumniDetails.getFullName());
        dto.setBirthDate(alumniDetails.getBirthDate());
        dto.setFaculty(alumniDetails.getFaculty().getId());
        return dto;
    }

    public AlumniDetails toDetailsEntity(AlumniDetailsDto dto, Faculty faculty){
        AlumniDetails entity = new AlumniDetails();
        entity.setFullName(dto.getFullName());
        entity.setBirthDate(dto.getBirthDate());
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
