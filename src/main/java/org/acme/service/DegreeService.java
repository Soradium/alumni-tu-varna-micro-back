package org.acme.service;

import java.util.List;

import org.acme.avro.ambiguous.DegreeDto;
import org.acme.entites.Degree;

public interface DegreeService {
    Degree getDegreeById(long id);
    Degree getDegreeByName(String name);
    List<DegreeDto> getAllDegrees();

    Degree createDegree(DegreeDto degreeDto);
    Degree updateDegree(DegreeDto degreeDto);
    void deleteDegree(long id);

    DegreeDto convertDegreeToDto(Degree degree);
    Degree convertDegreeFromDto(DegreeDto dto);
} 
