package org.acme.service;

import java.util.List;

import org.acme.avro.ambiguous.AlumniGroupDtoSimplified;
import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.entites.AlumniGroup;

public interface AlumniGroupService {

    AlumniGroup getAlumniGroupById(Integer id);

    List<AlumniGroupBackDto> getAllAlumniGroupsDtoBySpeciality(String speciality);
    List<AlumniGroupBackDto> getAllAlumniGroupsDtoByFaculty(String faculty);
    List<AlumniGroupBackDto> getAllAlumniGroupsDtoByGraduationYear(int graduationYear);

    AlumniGroup createAlumniGroups(AlumniGroupDtoSimplified dto);
    AlumniGroup updateAlumniGroups(AlumniGroupDtoSimplified dto);

    AlumniGroup createAlumniGroups(AlumniGroup group);
    AlumniGroup updateAlumniGroups(AlumniGroup group);

    void deleteAlumniGroups(Integer id);
    void deleteAlumniGroups(AlumniGroupDtoSimplified group);
    void deleteAlumniGroups(AlumniGroup group);

    AlumniGroupBackDto convertAlumniGroupsToDto(
        AlumniGroup group);
    AlumniGroup convertAlumniGroupsFromDto(
        AlumniGroupDtoSimplified dto);
}
