package org.acme.service;

import org.acme.dto.AlumniDetailsDto;
import org.acme.dto.AlumniDto;
import org.acme.dto.AlumniGroupDto;

import java.util.List;

public interface AlumniService {
    AlumniDto getAlumniById(long alumniId) throws Exception;
    List<AlumniDto> getAllAlumni() throws Exception;

    AlumniDto createAlumni(AlumniDto dto) throws Exception;
    AlumniDto updateAlumni(long alumniId, AlumniDto dto) throws Exception;
    AlumniDto deleteAlumni(long alumniId) throws Exception;

    AlumniDetailsDto createAlumniDetails(AlumniDetailsDto dto) throws Exception;
    AlumniDetailsDto getAlumniDetails(long alumniId) throws Exception;
    AlumniDetailsDto updateAlumniDetails(long alumniId, AlumniDetailsDto dto) throws Exception;

}
