package org.acme.service;

import org.acme.dto.DegreeDto;
import org.acme.entites.Degree;

import java.util.List;

public interface DegreeService {
    DegreeDto getDegreeById(long id);
    DegreeDto getDegreeByName(String name);
    Degree getDegreeByNameE(String name);
    List<DegreeDto> getAllDegrees();

    DegreeDto createDegree(DegreeDto degreeDto);
    DegreeDto updateDegree(long id, DegreeDto degreeDto);
    DegreeDto deleteDegree(long id);

}
