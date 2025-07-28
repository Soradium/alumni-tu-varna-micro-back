package org.acme.service;

import org.acme.dto.DegreeDto;

import java.util.List;

public interface DegreesService {
    DegreeDto getDegreeById(long id);
    DegreeDto getDegreeByName(String name);
    List<DegreeDto> getAllDegrees();

    DegreeDto createDegree(DegreeDto degreeDto);
    DegreeDto updateDegree(long id, DegreeDto degreeDto);
    DegreeDto deleteDegree(long id);

    boolean isDegreeExist(long id);
    boolean isDegreeNameExist(String name);
}
