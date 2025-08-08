package org.acme.service;

import java.util.List;

import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;

public interface AlumniDetailsService {
    public AlumniDetails getAlumniDetailsByFacultyNumber(int facultyNumber) throws Exception;
    public List<AlumniDetails> getAllAlumniDetails() throws Exception;
    public List<AlumniDto> getAlumniListByFaculty(String facultyName) throws Exception;
    public List<AlumniDto> getAlumniListByFullName(String fullName) throws Exception;
    public List<AlumniDetails> getDetailsForListOfAlumni(List<Alumni> alumniList) throws Exception;
    public AlumniDetails getDetailsForAlumni(Alumni alumni) throws Exception;
    public AlumniDetails getDetailsForAlumniDto(AlumniDto alumni) throws Exception;

    public AlumniDetails updateAlumniDetails(AlumniDetails alumniDetails) throws Exception;
    public AlumniDetails updateAlumniDetails(AlumniFrontDto alumniDetails) throws Exception;

}
