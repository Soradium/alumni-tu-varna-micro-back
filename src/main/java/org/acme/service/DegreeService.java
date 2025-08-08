package org.acme.service;

import java.util.List;

import org.acme.avro.ambiguous.DegreeDto;
import org.acme.entites.Degree;

public interface DegreeService {
    Degree getDegreeById(long id) throws Exception;
    Degree getDegreeByName(String name) throws Exception;
    List<DegreeDto> getAllDegrees() throws Exception;

    Degree createDegree(DegreeDto degreeDto) throws Exception;
    Degree updateDegree(DegreeDto degreeDto) throws Exception;
    void deleteDegree(long id) throws Exception;

} 
