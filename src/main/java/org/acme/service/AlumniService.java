package org.acme.service;

import java.util.List;

import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface AlumniService {
    public Alumni getAlumniByFacultyNumber(long facultyNumber) throws Exception;
    public List<Alumni> getAllAlumni() throws Exception; 
    public List<AlumniDto> getAllAlumniDto() throws Exception;
    public List<AlumniDto> getAlumniByDegree(String degree) throws Exception;

    public Alumni saveAlumni(Alumni alumni) throws Exception;
    public Alumni saveAlumni(AlumniFrontDto alumni) throws Exception;
    public Alumni updateAlumni(Alumni alumni) throws Exception;
    public Alumni updateAlumni(AlumniFrontDto alumni) throws Exception;
    public void deleteAlumni(int facultyNumber) throws Exception;

    public Alumni convertAlumniFromDto(AlumniFrontDto dto) throws Exception;
    public AlumniDto convertAlumniToDto(Alumni alumni) throws Exception;
}