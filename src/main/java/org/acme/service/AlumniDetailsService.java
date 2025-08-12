package org.acme.service;

import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;

import java.util.List;

public interface AlumniDetailsService {
    AlumniDetails getAlumniDetailsByFacultyNumber(int facultyNumber) throws Exception;

    List<AlumniDetails> getAllAlumniDetails() throws Exception;

    List<AlumniDto> getAlumniListByFaculty(String facultyName) throws Exception;

    List<AlumniDto> getAlumniListByFullName(String fullName) throws Exception;

    List<AlumniDetails> getDetailsForListOfAlumni(List<Alumni> alumniList) throws Exception;

    AlumniDetails getDetailsForAlumni(Alumni alumni) throws Exception;

    AlumniDetails getDetailsForAlumniDto(AlumniDto alumni) throws Exception;

    AlumniDetails updateAlumniDetails(AlumniDetails alumniDetails) throws Exception;

    AlumniDetails updateAlumniDetails(AlumniFrontDto alumniDetails) throws Exception;

}
