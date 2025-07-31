package org.acme.service;

import org.acme.dto.AlumniGroupsMembershipDto;

import java.util.List;

public interface AlumniGroupsMembershipService {
    AlumniGroupsMembershipDto getMembershipByFN(int id);
    List<AlumniGroupsMembershipDto> getAllMemberships();

    AlumniGroupsMembershipDto createMembership(AlumniGroupsMembershipDto dto) throws Exception;
    AlumniGroupsMembershipDto updateMembership(int id, AlumniGroupsMembershipDto dto);
    AlumniGroupsMembershipDto deleteMembership(int id);

}
