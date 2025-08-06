package org.acme.service;

import java.util.List;

import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.avro.front.AlumniGroupMembershipFrontDto;
import org.acme.entites.AlumniGroupsMembership;


public interface AlumniGroupMembershipService {
    AlumniGroupsMembership getAlumniGroupsMembershipById(Integer id);
    AlumniGroupsMembership getAlumniGroupsMembershipByName(String name);
    
    List<AlumniGroupMembershipDto> getAllAlumniGroupMembershipsDto();
    List<AlumniGroupMembershipDto> getAllAlumniGroupMembershipsByGroup(int groupId);
    List<AlumniGroupMembershipDto> getAllAlumniGroupMembershipsByFacultyNumber(int facultyNumber);

    AlumniGroupsMembership createAlumniGroupsMembership(AlumniGroupMembershipFrontDto dto);
    AlumniGroupsMembership updateAlumniGroupsMembership(AlumniGroupMembershipFrontDto dto);

    AlumniGroupsMembership createAlumniGroupsMembership(AlumniGroupsMembership membership);
    AlumniGroupsMembership updateAlumniGroupsMembership(AlumniGroupsMembership membership);

    void deleteAlumniGroupsMembership(Integer id);
    void deleteAlumniGroupsMembership(AlumniGroupMembershipFrontDto membership);
    void deleteAlumniGroupsMembership(AlumniGroupsMembership membership);

    AlumniGroupMembershipDto convertAlumniGroupsMembershipToDto(
        AlumniGroupsMembership membership);
    AlumniGroupsMembership convertAlumniGroupsMembershipFromDto(
        AlumniGroupMembershipFrontDto dto);
}
