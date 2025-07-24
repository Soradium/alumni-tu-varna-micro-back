package org.acme.service;

import org.acme.dto.AlumniDetailsDto;
import org.acme.dto.AlumniDto;
import org.acme.dto.AlumniGroupDto;

import java.util.List;

public interface AlumniService {
    AlumniDto getAlumniById(Long alumniId) throws Exception;
    List<AlumniDto> getAllAlumni() throws Exception;
    AlumniDto createAlumni(AlumniDto dto) throws Exception;
    AlumniDto updateAlumni(Long alumniId, AlumniDto dto) throws Exception;
    AlumniDto deleteAlumni(Long alumniId) throws Exception;

    //createAlumniDetails()? or handle in createAlumni()
    AlumniDetailsDto getAlumniDetails(Long alumniId) throws Exception;
    AlumniDetailsDto updateAlumniDetails(Long alumniId, AlumniDetailsDto dto) throws Exception;

}
