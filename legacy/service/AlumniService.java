package org.acme.service;

import java.util.List;

import org.acme.avro.back.AlumniDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;

public interface AlumniService {
    AlumniDto getAlumniById(int alumniId) throws Exception;
    Alumni getAlumniByIdE(int alumniId) throws Exception;
    AlumniDetails getAlumniDetailsByIdE(int alumniId) throws Exception;
    List<AlumniDto> getAllAlumni() throws Exception;

    AlumniDto createAlumni(AlumniDto dto) throws Exception; // responsible for both details and alumni
    AlumniDto updateAlumni(AlumniDto dto) throws Exception;
    AlumniDto deleteAlumni(int alumniId) throws Exception;

    // AlumniDetailsDto createAlumniDetails(AlumniDetailsDto dto) throws Exception;
    // AlumniDetailsDto getAlumniDetails(int alumniId) throws Exception;
    // AlumniDetailsDto updateAlumniDetails(int alumniId, AlumniDetailsDto dto) throws Exception;

}
