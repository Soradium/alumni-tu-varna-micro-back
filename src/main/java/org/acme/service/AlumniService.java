package org.acme.service;

import org.acme.dto.AlumniDetailsDto;
import org.acme.dto.AlumniDto;
import org.acme.dto.AlumniGroupDto;
import org.acme.entites.Alumni;

import java.util.List;

public interface AlumniService {
    AlumniDto getAlumniById(int alumniId) throws Exception;
    Alumni getAlumniByIdE(int alumniId) throws Exception;

    List<AlumniDto> getAllAlumni() throws Exception;

    AlumniDto createAlumni(AlumniDto dto) throws Exception;
    AlumniDto updateAlumni(int alumniId, AlumniDto dto) throws Exception;
    AlumniDto deleteAlumni(int alumniId) throws Exception;

    AlumniDetailsDto createAlumniDetails(AlumniDetailsDto dto) throws Exception;
    AlumniDetailsDto getAlumniDetails(int alumniId) throws Exception;
    AlumniDetailsDto updateAlumniDetails(int alumniId, AlumniDetailsDto dto) throws Exception;

}
