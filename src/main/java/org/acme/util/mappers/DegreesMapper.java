package org.acme.util.mappers;

import org.acme.dto.DegreeDto;
import org.acme.entites.Degrees;

public class DegreesMapper {
    public DegreeDto toDto(Degrees degree) {
        DegreeDto degreeDto = new DegreeDto();
        degreeDto.setId(degree.getId());
        degreeDto.setDegree(degree.getDegree());
        return degreeDto;
    }

    public Degrees toEntity(DegreeDto degreeDto) {
        Degrees degree = new Degrees();
        degree.setDegree(degreeDto.getDegree());
        return degree;
    }
}
