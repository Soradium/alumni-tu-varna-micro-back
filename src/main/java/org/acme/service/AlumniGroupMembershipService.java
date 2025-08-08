package org.acme.service;

import java.util.List;

import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.avro.front.AlumniGroupMembershipFrontDto;
import org.acme.entites.AlumniGroupsMembership;


public interface AlumniGroupMembershipService {
    AlumniGroupsMembership getAlumniGroupsMembershipById(Integer id) throws Exception;
    AlumniGroupsMembership getAlumniGroupsMembershipByName(String name) throws Exception;
    
    List<AlumniGroupMembershipDto> getAllAlumniGroupMembershipsDto() throws Exception;
    List<AlumniGroupMembershipDto> getAllAlumniGroupMembershipsByGroup(int groupId) throws Exception;
    List<AlumniGroupMembershipDto> getAllAlumniGroupMembershipsByFacultyNumber(int facultyNumber) throws Exception;

    AlumniGroupsMembership createAlumniGroupsMembership(AlumniGroupMembershipFrontDto dto) throws Exception;
    AlumniGroupsMembership updateAlumniGroupsMembership(AlumniGroupMembershipFrontDto dto) throws Exception;

    AlumniGroupsMembership createAlumniGroupsMembership(AlumniGroupsMembership membership) throws Exception;
    AlumniGroupsMembership updateAlumniGroupsMembership(AlumniGroupsMembership membership) throws Exception;

    void deleteAlumniGroupsMembership(Integer id) throws Exception;
    void deleteAlumniGroupsMembership(AlumniGroupMembershipFrontDto membership) throws Exception;
    void deleteAlumniGroupsMembership(AlumniGroupsMembership membership) throws Exception;

}
