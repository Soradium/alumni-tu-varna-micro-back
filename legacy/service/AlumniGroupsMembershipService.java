package org.acme.service;

import java.util.List;

import org.acme.dto.AlumniGroupsMembershipDto;
import org.acme.entites.AlumniGroupsMembership;

public interface AlumniGroupsMembershipService {
    List<AlumniGroupsMembershipDto> getMembershipsByFN(int id);
    List<AlumniGroupsMembershipDto> getAllMemberships();
    AlumniGroupsMembership getMembershipById(int id);

    AlumniGroupsMembershipDto createMembership(AlumniGroupsMembershipDto dto) throws Exception;
    AlumniGroupsMembershipDto updateMembership(int id, AlumniGroupsMembershipDto dto);
    AlumniGroupsMembershipDto deleteMembership(int id);

}
