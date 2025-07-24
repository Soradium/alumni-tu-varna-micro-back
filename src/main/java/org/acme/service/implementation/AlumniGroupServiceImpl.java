package org.acme.service.implementation;

import org.acme.dto.AlumniGroupDto;
import org.acme.service.AlumniGroupService;

import java.util.List;

public class AlumniGroupServiceImpl implements AlumniGroupService {

    @Override
    public List<AlumniGroupDto> getGroupsByAlumniId(Long alumniId) throws Exception {
        return List.of();
    }

    @Override
    public AlumniGroupDto createAlumniGroup(AlumniGroupDto dto) throws Exception {
        return null;
    }

    @Override
    public AlumniGroupDto updateAlumniGroup(Long alumniId, AlumniGroupDto dto) throws Exception {
        return null;
    }

    @Override
    public AlumniGroupDto assignToGroup(Long alumniId, Long groupId) throws Exception {
        return null;
    }
}
