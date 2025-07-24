package org.acme.service;

import org.acme.dto.AlumniGroupDto;

import java.util.List;

public interface AlumniGroupService {
    List<AlumniGroupDto> getGroupsByAlumniId(Long alumniId) throws Exception;
    AlumniGroupDto createAlumniGroup(AlumniGroupDto dto) throws Exception;
    AlumniGroupDto updateAlumniGroup(Long alumniId, AlumniGroupDto dto) throws Exception;
    AlumniGroupDto assignToGroup(Long alumniId, Long groupId) throws Exception;
}
