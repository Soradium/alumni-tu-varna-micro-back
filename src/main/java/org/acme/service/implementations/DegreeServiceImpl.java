package org.acme.service.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.acme.avro.ambiguous.DegreeDto;
import org.acme.entites.Degree;
import org.acme.repository.DegreeRepository;
import org.acme.service.DegreeService;
import org.acme.util.mappers.DegreeMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DegreeServiceImpl implements DegreeService {

    private final DegreeRepository degreeRepository;
    private final DegreeMapper degreeMapper;

    @Inject
    public DegreeServiceImpl(DegreeRepository degreeRepository, DegreeMapper degreeMapper) {
        this.degreeRepository = degreeRepository;
        this.degreeMapper = degreeMapper;
    }

    @Override
    public Degree createDegree(DegreeDto degreeDto) throws Exception {
        Degree degree = degreeMapper.toEntity(degreeDto);
        degreeRepository.persist(degree);
        return degree;
    }

    @Override
    public void deleteDegree(long id) throws Exception {
        if (id <= 0) {
            throw new Exception("Degree ID entered is incorrect.");
        }
        degreeRepository.deleteById(id);
    }

    @Override
    public List<DegreeDto> getAllDegrees() throws Exception {
        return convertToDtoList(degreeRepository.listAll());
    }

    @Override
    public Degree getDegreeById(long id) throws Exception {
        if (id <= 0) {
            throw new Exception("Degree ID entered is incorrect.");
        }
        return degreeRepository.findById(id);
    }

    @Override
    public Degree getDegreeByName(String name) throws Exception {
        if (name == null) {
            throw new Exception("Name is null.");
        }
        return degreeRepository.findByNameOptional(name)
                .orElseThrow(() -> new Exception(
                        "No such degree name is found in the system.")
                );

    }

    @Override
    public Degree updateDegree(DegreeDto degreeDto) throws Exception {
        if (degreeDto == null) {
            throw new Exception("degreeDto is null.");
        }
        Degree existingDegree = degreeRepository
                .findByIdOptional((long) degreeDto.getId())
                .orElseThrow(() -> new Exception(
                        "No degree with such ID was found.")
                );
        existingDegree.setDegree(degreeDto.getDegreeName());

        degreeRepository.persist(existingDegree);
        return existingDegree;

    }

    public List<DegreeDto> convertToDtoList(List<Degree> degrees) throws Exception {
        return degrees.stream().map(
                        m -> new DegreeDto(m.getId(), m.getDegreeName()))
                .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((degreeRepository == null) ? 0 : degreeRepository.hashCode());
        result = prime * result + ((degreeMapper == null) ? 0 : degreeMapper.hashCode());
        return result;
    }


}
