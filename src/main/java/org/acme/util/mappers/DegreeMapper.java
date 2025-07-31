package org.acme.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.DegreeDto;
import org.acme.entites.Degree;

@ApplicationScoped
public class DegreeMapper {
    public DegreeDto toDto(Degree degree) {
        DegreeDto degreeDto = new DegreeDto();
        degreeDto.setId(degree.getId());
        degreeDto.setDegree(degree.getDegree());
        return degreeDto;
    }

    public Degree toEntity(DegreeDto degreeDto) {
        Degree degree = new Degree();
        degree.setDegree(degreeDto.getDegree());
        return degree;
    }
}
