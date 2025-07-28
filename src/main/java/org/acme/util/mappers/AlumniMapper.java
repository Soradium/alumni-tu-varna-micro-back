package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.dto.AlumniDto;
import org.acme.dto.ApiBaseDto;
import org.acme.dto.DegreeDto;
import org.acme.entites.Alumni;
import org.acme.entites.ApiBase;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.AlumniRepository;
import org.acme.repository.ApiBaseRepository;
import org.acme.service.ApiBaseService;
import org.acme.service.DegreesService;

@ApplicationScoped
public class AlumniMapper {
    @Inject
    ApiBaseService apiBaseService;
    @Inject
    DegreesService degreesService;
    @Inject
    ApiBaseMapper apiBaseMapper;
    @Inject
    DegreesMapper degreesMapper;



    public AlumniDto toDto(Alumni alumni){
        AlumniDto alumniDto = new AlumniDto();
        alumniDto.setId(alumni.getId());
        alumniDto.setApiBase(apiBaseMapper.toDto(alumni.getApiBase()));
        alumniDto.setDegreeId(alumni.getDegrees().getId());
        return alumniDto;
    }
    public Alumni toEntity(AlumniDto alumniDto){
        Alumni alumni = new Alumni();
        ApiBaseDto apiBaseDto;

        if(apiBaseService.isApiBaseExist(alumniDto.getApiBase())){
            apiBaseDto = apiBaseService.getApiBase(
                    alumniDto.getApiBase()
            );
        }
        else apiBaseDto = apiBaseService.createApiBase(alumniDto.getApiBase());
        alumni.setApiBase(apiBaseMapper.toEntity(apiBaseDto));

        DegreeDto degreeDto = degreesService.getDegreeById(alumniDto.getDegreeId());
        alumni.setDegrees(degreesMapper.toEntity(degreeDto));

        return alumni;
    }

    public Alumni updateEntity(AlumniDto alumniDto, Alumni alumni){
        if(alumniDto.getApiBase() != null){
            alumni.getApiBase().setFacebookUrl(alumniDto.getApiBase().getFacebookUrl());
            alumni.getApiBase().setLinkedinUrl(alumniDto.getApiBase().getLinkedinUrl());
        }
        if(alumniDto.getDegreeId() != null){
            DegreeDto degreeDto = degreesService.getDegreeById(alumniDto.getDegreeId());
            alumni.setDegrees(degreesMapper.toEntity(degreeDto));
        }
        return alumni;
    }
}
