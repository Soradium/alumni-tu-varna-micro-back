package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;

import java.util.List;

@ApplicationScoped
public interface AlumniService {
    Alumni getAlumniByFacultyNumber(long facultyNumber) throws Exception;

    AlumniDto getAlumniDtoByFacultyNumber(long facultyNumber) throws Exception;

    List<Alumni> getAllAlumni() throws Exception;

    List<AlumniDto> getAllAlumniDto() throws Exception;

    List<AlumniDto> getAlumniByDegree(String degree) throws Exception;

    Alumni saveAlumni(Alumni alumni, AlumniDetails details) throws Exception;

    Alumni saveAlumni(AlumniFrontDto alumni) throws Exception;

    Alumni updateAlumni(Alumni alumni) throws Exception;

    Alumni updateAlumni(AlumniFrontDto alumni) throws Exception;

    void deleteAlumni(int facultyNumber) throws Exception;

}