package org.acme.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.SpecialityDto;
import org.acme.entites.Speciality;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.SpecialityRepository;
import org.acme.service.SpecialityService;
import org.acme.util.mappers.SpecialityMapper;

import java.util.List;

@ApplicationScoped
public class SpecialityServiceImpl implements SpecialityService {

    @Inject
    SpecialityRepository specialityRepository;
    @Inject
    SpecialityMapper specialityMapper;

    @Override
    public SpecialityDto getSpecialityById(long id) {
        //TODO: check id
        Speciality dbSpeciality = specialityRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality with ID: "
                        + id + " not found"));
        return specialityMapper.toDto(dbSpeciality);
    }

    @Override
    public Speciality getSpecialityByNameE(String name) {
        Speciality dbSpeciality = specialityRepository.findByNameOptional(name)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality "
                        + name + " not found"));
        return dbSpeciality;
    }

    @Override
    public SpecialityDto getSpecialityByName(String name) {
        Speciality dbSpeciality = specialityRepository.findByNameOptional(name)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality "
                        + name + " not found"));
        return specialityMapper.toDto(dbSpeciality);
    }

    @Override
    public List<SpecialityDto> getAllSpecialities() {
        List<Speciality> specialitiesList = specialityRepository.listAll();
        if(specialitiesList.isEmpty()){
            throw new ResourceNotFoundException("No specialities found");
        }
        return specialitiesList.stream().map(specialityMapper::toDto).toList();
    }

    @Override
    @Transactional
    public SpecialityDto createSpeciality(SpecialityDto specialityDto) {
        Speciality speciality = specialityMapper.toEntity(specialityDto);
        specialityRepository.persist(speciality);
        return specialityMapper.toDto(speciality);
    }

    @Override
    @Transactional
    public SpecialityDto updateSpeciality(long id, SpecialityDto specialityDto) {
        Speciality dbSpeciality = specialityRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality with ID: "
                        + id + " not found"));
        dbSpeciality.setSpecialityName(specialityDto.getSpeciality());
        specialityRepository.persist(dbSpeciality);
        return specialityMapper.toDto(dbSpeciality);
    }

    @Override
    @Transactional
    public SpecialityDto deleteSpeciality(long id) {
        Speciality dbSpeciality = specialityRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality with ID: "
                        + id + " not found"));
        specialityRepository.delete(dbSpeciality);
        return specialityMapper.toDto(dbSpeciality);
    }

    @Override
    public boolean checkSpeciality(String specialityName) {
        Speciality dbSpeciality = specialityRepository.findByNameOptional(specialityName)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality "
                        + specialityName + " not found"));
        if(dbSpeciality != null){
            return true;
        }
        return false;
    }
}
