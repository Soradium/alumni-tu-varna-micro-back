package org.acme.service;

import java.util.List;

import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface AlumniService {
    public Alumni getAlumniByFacultyNumber(long facultyNumber) throws Exception;
    public AlumniDto getAlumniDtoByFacultyNumber(long facultyNumber) throws Exception;
    public List<Alumni> getAllAlumni() throws Exception; 
    public List<AlumniDto> getAllAlumniDto() throws Exception;
    public List<AlumniDto> getAlumniByDegree(String degree) throws Exception;

    public Alumni saveAlumni(Alumni alumni, AlumniDetails details) throws Exception;
    public Alumni saveAlumni(AlumniFrontDto alumni) throws Exception;
    public Alumni updateAlumni(Alumni alumni) throws Exception;
    public Alumni updateAlumni(AlumniFrontDto alumni) throws Exception;
    public void deleteAlumni(int facultyNumber) throws Exception;

}