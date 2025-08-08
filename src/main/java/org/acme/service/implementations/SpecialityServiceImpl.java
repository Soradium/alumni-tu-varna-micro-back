package org.acme.service.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.acme.avro.ambiguous.SpecialityDto;
import org.acme.entites.Speciality;
import org.acme.repository.SpecialityRepository;
import org.acme.service.SpecialityService;
import org.acme.util.mappers.SpecialityMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
@ApplicationScoped
public class SpecialityServiceImpl implements SpecialityService {

    private final SpecialityRepository specialityRepository;
    private final SpecialityMapper specialityMapper;

    @Inject
    public SpecialityServiceImpl(SpecialityRepository specialityRepository, SpecialityMapper specialityMapper) {
        this.specialityRepository = specialityRepository;
        this.specialityMapper = specialityMapper;
    }

    @Override
    public Speciality createSpeciality(SpecialityDto specialityDto) throws Exception {
        if (specialityDto == null) {
            throw new Exception("SpecialityDto is null.");
        }

        Speciality speciality = specialityMapper.toEntity(specialityDto);
        specialityRepository.persist(speciality);
        return speciality;
    }

    @Override
    public void deleteSpeciality(long id) throws Exception {
        if (id <= 0) {
            throw new Exception("Speciality ID is invalid.");
        }

        boolean deleted = specialityRepository.deleteById(id);
        if (!deleted) {
            throw new Exception("Speciality with ID " + id + " does not exist.");
        }
    }

    @Override
    public List<SpecialityDto> getAllSpecialities() throws Exception {
        List<Speciality> specialities = specialityRepository.findAll().list();
        return convertToDtoList(specialities);
    }

    @Override
    public Speciality getSpecialityById(long id) throws Exception {
        if (id <= 0) {
            throw new Exception("Speciality ID is invalid.");
        }
        return specialityRepository.findById(id);
    }

    @Override
    public Speciality getSpecialityByName(String name) throws Exception {
        if (name == null || name.isEmpty()) {
            throw new Exception("Speciality name is null or empty.");
        }

        return specialityRepository.findByNameOptional(name)
                .orElseThrow(() -> new Exception("Speciality with name '" + name + "' not found."));
    }

    @Override
    public Speciality updateSpeciality(SpecialityDto specialityDto) throws Exception {
        if (specialityDto == null) {
            throw new Exception("SpecialityDto is null.");
        }

        Speciality existing = specialityRepository.findByIdOptional((long) specialityDto.getId())
                .orElseThrow(() -> new Exception("No speciality with such ID was found."));

        existing.setSpecialityName(specialityDto.getSpecialityName());
        specialityRepository.persist(existing);
        return existing;
    }

    public List<SpecialityDto> convertToDtoList(List<Speciality> specialities) {
        return specialities.stream()
                .map(s -> new SpecialityDto(s.getId(), s.getSpecialityName()))
                .collect(Collectors.toList());
    }
}
