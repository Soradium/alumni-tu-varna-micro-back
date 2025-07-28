package org.acme.service.implementation;

import org.acme.dto.SpecialityDto;
import org.acme.entites.Specialities;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.SpecialitiesRepository;
import org.acme.service.SpecialitiesService;
import org.acme.util.mappers.SpecialitiesMapper;

import java.util.List;
import java.util.stream.Collectors;

public class SpecialitiesServiceImpl implements SpecialitiesService {

    private final SpecialitiesRepository specialitiesRepository;
    private final SpecialitiesMapper specialitiesMapper;

    public SpecialitiesServiceImpl(SpecialitiesRepository specialitiesRepository, SpecialitiesMapper specialitiesMapper) {
        this.specialitiesRepository = specialitiesRepository;
        this.specialitiesMapper = specialitiesMapper;
    }

    @Override
    public SpecialityDto getSpecialityById(long id) {
        //TODO: check id
        Specialities dbSpeciality = specialitiesRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality with ID: "
                        + id + " not found"));
        return specialitiesMapper.toDto(dbSpeciality);
    }

    @Override
    public SpecialityDto getSpecialityByName(String name) {
        Specialities dbSpeciality = specialitiesRepository.findByNameOptional(name)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality "
                        + name + " not found"));
        return specialitiesMapper.toDto(dbSpeciality);
    }

    @Override
    public List<SpecialityDto> getAllSpecialities() {
        List<Specialities> specialitiesList = specialitiesRepository.listAll();
        if(specialitiesList.isEmpty()){
            throw new ResourceNotFoundException("No specialities found");
        }
        return specialitiesList.stream().map(specialitiesMapper::toDto).toList();
    }

    @Override
    public SpecialityDto createSpeciality(SpecialityDto specialityDto) {
        Specialities speciality = specialitiesMapper.toEntity(specialityDto);
        specialitiesRepository.persist(speciality);
        return specialitiesMapper.toDto(speciality);
    }

    @Override
    public SpecialityDto updateSpeciality(long id, SpecialityDto specialityDto) {
        Specialities dbSpeciality = specialitiesRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality with ID: "
                        + id + " not found"));
        dbSpeciality.setSpeciality(specialityDto.getSpeciality());
        specialitiesRepository.persist(dbSpeciality);
        return specialitiesMapper.toDto(dbSpeciality);
    }

    @Override
    public SpecialityDto deleteSpeciality(long id) {
        Specialities dbSpeciality = specialitiesRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality with ID: "
                        + id + " not found"));
        specialitiesRepository.delete(dbSpeciality);
        return specialitiesMapper.toDto(dbSpeciality);
    }

    @Override
    public boolean checkSpeciality(String specialityName) {
        Specialities dbSpeciality = specialitiesRepository.findByNameOptional(specialityName)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality "
                        + specialityName + " not found"));
        if(dbSpeciality != null){
            return true;
        }
        return false;
    }
}
