package org.acme.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.DegreeDto;
import org.acme.entites.Degree;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.DegreeRepository;
import org.acme.service.DegreeService;
import org.acme.util.mappers.DegreeMapper;

import java.util.List;

@ApplicationScoped
public class DegreeServiceImpl implements DegreeService {

    @Inject
    DegreeRepository degreeRepository;
    @Inject
    DegreeMapper degreeMapper;

    @Override
    public DegreeDto getDegreeById(long id) {
        //TODO: check id
        Degree dbDegree = degreeRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Degree with id " +
                       id + "not found"));
        return degreeMapper.toDto(dbDegree);
    }

    @Override
    public DegreeDto getDegreeByName(String name) {
        Degree dbDegree = degreeRepository.findByNameOptional(name)
                .orElseThrow(() -> new ResourceNotFoundException("Degree " +
                        name + "not found"));
        return degreeMapper.toDto(dbDegree);
    }

    @Override
    public Degree getDegreeByNameE(String name) {
        Degree dbDegree = degreeRepository.findByNameOptional(name)
                .orElseThrow(() -> new ResourceNotFoundException("Degree " +
                        name + "not found"));
        return dbDegree;
    }

    @Override
    public List<DegreeDto> getAllDegrees() {
        List<Degree> degreesList = degreeRepository.listAll();
        if(degreesList.isEmpty()) {
            throw new ResourceNotFoundException("No degrees found");
        }
        return degreesList.stream().map(degreeMapper::toDto).toList();
    }

    @Override
    @Transactional
    public DegreeDto createDegree(DegreeDto degreeDto) {
        Degree degree = degreeMapper.toEntity(degreeDto);
        degreeRepository.persist(degree);
        return degreeMapper.toDto(degree);
    }

    @Override
    @Transactional
    public DegreeDto updateDegree(long id, DegreeDto degreeDto) {
        Degree dbDegree = degreeRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Degree "+degreeDto.getDegree()+" not found"));
        dbDegree.setDegree(degreeDto.getDegree());
        degreeRepository.persist(dbDegree);
        return degreeMapper.toDto(dbDegree);
    }

    @Override
    @Transactional
    public DegreeDto deleteDegree(long id) {
        //check id
        Degree dbDegree = degreeRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Degree with ID: "+id+" not found"));
        degreeRepository.delete(dbDegree);
        return degreeMapper.toDto(dbDegree);
    }


}
