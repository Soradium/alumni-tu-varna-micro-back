package org.acme.service;

import org.acme.dto.AlumniGroupDto;
import org.acme.dto.AlumniGroupsMembershipDto;
import org.acme.entites.AlumniGroup;

import java.util.List;

public interface AlumniGroupService {
    AlumniGroupDto getAlumniGroupByGroupNumber(int groupNumber);
    AlumniGroup getAlumniGroupByGroupNumberE(int groupNumber);

    AlumniGroupDto createAlumniGroup(AlumniGroupDto dto) throws Exception;
    AlumniGroupDto updateAlumniGroup(int groupNumber, AlumniGroupDto dto) throws Exception;

    AlumniGroupDto assignToGroup(AlumniGroupsMembershipDto dto) throws Exception;
}
