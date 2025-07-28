package org.acme.service.implementation;

import org.acme.dto.DegreeDto;
import org.acme.entites.Degrees;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.DegreesRepository;
import org.acme.service.DegreesService;
import org.acme.util.mappers.DegreesMapper;

import java.util.List;

public class DegreesServiceImpl implements DegreesService {

    private final DegreesRepository degreesRepository;
    private final DegreesMapper degreesMapper;

    public DegreesServiceImpl(DegreesRepository degreesRepository, DegreesMapper degreesMapper) {
        this.degreesRepository = degreesRepository;
        this.degreesMapper = degreesMapper;
    }

    @Override
    public DegreeDto getDegreeById(long id) {
        //TODO: check id
        Degrees dbDegree = degreesRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Degree with id " +
                       id + "not found"));
        return degreesMapper.toDto(dbDegree);
    }

    @Override
    public DegreeDto getDegreeByName(String name) {
        Degrees dbDegree = degreesRepository.findByNameOptional(name)
                .orElseThrow(() -> new ResourceNotFoundException("Degree " +
                        name + "not found"));
        return degreesMapper.toDto(dbDegree);
    }

    @Override
    public List<DegreeDto> getAllDegrees() {
        List<Degrees> degreesList = degreesRepository.listAll();
        if(degreesList.isEmpty()) {
            throw new ResourceNotFoundException("Degree list is empty");
        }
        return degreesList.stream().map(degreesMapper::toDto).toList();
    }

    @Override
    public DegreeDto createDegree(DegreeDto degreeDto) {
        Degrees degree = degreesMapper.toEntity(degreeDto);
        degreesRepository.persist(degree);
        return degreesMapper.toDto(degree);
    }

    @Override
    public DegreeDto updateDegree(long id, DegreeDto degreeDto) {
        Degrees dbDegree = degreesRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Degree "+degreeDto.getDegree()+" not found"));
        dbDegree.setDegree(degreeDto.getDegree());
        degreesRepository.persist(dbDegree);
        return degreesMapper.toDto(dbDegree);
    }

    @Override
    public DegreeDto deleteDegree(long id) {
        //check id
        Degrees dbDegree = degreesRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Degree with ID: "+id+" not found"));
        degreesRepository.delete(dbDegree);
        return degreesMapper.toDto(dbDegree);
    }

    @Override
    public boolean isDegreeExist(long id) {
        //check id
        Degrees dbDegree = degreesRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Degree with ID: "+id+" not found"));
        if(dbDegree != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isDegreeNameExist(String name) {
        Degrees dbDegree = degreesRepository.findByNameOptional(name)
                .orElseThrow(() -> new ResourceNotFoundException("Degree "+name+" not found"));
        if(dbDegree != null) {
            return true;
        }
        return false;
    }
}
