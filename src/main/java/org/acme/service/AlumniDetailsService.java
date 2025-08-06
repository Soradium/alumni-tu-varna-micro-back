package org.acme.service;

import java.util.List;

import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;

public interface AlumniDetailsService extends AlumniService {
    public Alumni getAlumniDetailsByFacultyNumber(int facultyNumber) throws Exception;
    public List<AlumniDetails> getAllAlumniDetails() throws Exception;
    public List<AlumniDto> getAlumniByFaculty() throws Exception;
    public AlumniDto getAlumniByFullName() throws Exception;

    public AlumniDetails saveAlumniDetails(AlumniDetails alumniDetails) throws Exception;
    public AlumniDetails saveAlumniDetails(AlumniFrontDto AlumniDetails) throws Exception;
    public AlumniDetails updateAlumniDetails(AlumniDetails AlumniDetails) throws Exception;
    public AlumniDetails updateAlumniDetails(AlumniFrontDto AlumniDetails) throws Exception;
    public void deleteAlumniDetails(int facultyNumber) throws Exception;

    public AlumniDetails convertAlumniDetailsFromDto(AlumniFrontDto dto) throws Exception;

}
