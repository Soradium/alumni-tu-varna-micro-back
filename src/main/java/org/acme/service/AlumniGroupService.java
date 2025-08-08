package org.acme.service;

import java.util.List;

import org.acme.avro.ambiguous.AlumniGroupDtoSimplified;
import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.entites.AlumniGroup;

public interface AlumniGroupService {

    AlumniGroup getAlumniGroupById(Integer id) throws Exception;
    AlumniGroupBackDto getAlumniGroupDtoById(Integer id) throws Exception;

    List<AlumniGroupBackDto> getAllAlumniGroupsDtoBySpeciality(String speciality) throws Exception;
    List<AlumniGroupBackDto> getAllAlumniGroupsDtoByFaculty(String faculty) throws Exception;
    List<AlumniGroupBackDto> getAllAlumniGroupsDtoByGraduationYear(int graduationYear) throws Exception;

    AlumniGroup createAlumniGroup(AlumniGroupDtoSimplified dto) throws Exception;
    AlumniGroup updateAlumniGroup(AlumniGroupDtoSimplified dto) throws Exception;

    AlumniGroup createAlumniGroup(AlumniGroup group) throws Exception;
    AlumniGroup updateAlumniGroup(AlumniGroup group) throws Exception;

    void deleteAlumniGroup(Integer id) throws Exception;
    void deleteAlumniGroup(AlumniGroupDtoSimplified group) throws Exception;
    void deleteAlumniGroup(AlumniGroup group) throws Exception;
}
