package org.acme.service.implementation;

import org.acme.dto.AlumniDetailsDto;
import org.acme.dto.AlumniDto;
import org.acme.entites.Alumni;
import org.acme.exceptions.DuplicateResourceException;
import org.acme.exceptions.IncorrectAlumnusNumberException;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.AlumniRepository;
import org.acme.service.AlumniService;
import org.acme.util.mappers.AlumniMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AlumniServiceImpl implements AlumniService {

    private final AlumniRepository alumniRepository;
    private final AlumniMapper alumniMapper;

    public AlumniServiceImpl(AlumniRepository alumniRepository, AlumniMapper alumniMapper) {
        this.alumniRepository = alumniRepository;
        this.alumniMapper = alumniMapper;
    }

    @Override
    public AlumniDto getAlumniById(long alumniId) throws Exception {
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
        Alumni alumni = alumniRepository.findByIdOptional(alumniId).orElseThrow(
                () -> new ResourceNotFoundException("Alumni with id " + alumniId + " not found")
        );
        return alumniMapper.toDto(alumni);
    }

    @Override
    public List<AlumniDto> getAllAlumni() throws Exception {
        List<Alumni> dbList = alumniRepository.listAll();
        if(dbList.isEmpty())
            throw new ResourceNotFoundException("No alumni found");
        return dbList.stream().map(alumniMapper::toDto).toList();
    }

    @Override
    public AlumniDto createAlumni(AlumniDto dto) {
        Alumni newAlumni = alumniMapper.toEntity(dto);
        alumniRepository.persist(newAlumni);
        return alumniMapper.toDto(newAlumni);
    }

    @Override
    public AlumniDto updateAlumni(long alumniId, AlumniDto dto) throws Exception {
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
        //TODO: fetch Alumni or throw exception
        Alumni alumni = alumniRepository.findByIdOptional(alumniId).orElseThrow(
                () -> new ResourceNotFoundException("Alumni with id " + alumniId + " not found")
        );
        alumniMapper.updateEntity(dto, alumni);
        alumniRepository.persist(alumni);
        return alumniMapper.toDto(alumni);
    }

    @Override
    public AlumniDto deleteAlumni(long alumniId) throws Exception {
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
        Alumni alumni = alumniRepository.findByIdOptional(alumniId).orElseThrow(
                () -> new ResourceNotFoundException("Alumni with id " + alumniId + " not found")
        );
        //TODO: remove associated details or delete on cascade
        alumniRepository.delete(alumni);
        return alumniMapper.toDto(alumni);
    }

    @Override
    public AlumniDetailsDto createAlumniDetails(AlumniDetailsDto dto) throws Exception {
        return null;
    }

    @Override
    public AlumniDetailsDto getAlumniDetails(long alumniId) throws Exception {
        //TODO: check id
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
        //TODO: fetch AlumniDetails or throw exception
        //TODO: map to AlumniDetailsDto
        return null;
    }

    @Override
    public AlumniDetailsDto updateAlumniDetails(long alumniId, AlumniDetailsDto dto) throws Exception {
        //TODO: check id
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
        //TODO: fetch AlumniDetails or throw exception
        //TODO: map dto to AlumniDetails and persist changes
        //TODO: map updated to dto
        return null;
    }
}
