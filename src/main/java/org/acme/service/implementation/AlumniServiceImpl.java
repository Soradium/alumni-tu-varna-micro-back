package org.acme.service.implementation;

import org.acme.dto.AlumniDetailsDto;
import org.acme.dto.AlumniDto;
import org.acme.service.AlumniService;

import java.util.Collections;
import java.util.List;

public class AlumniServiceImpl implements AlumniService {
    @Override
    public AlumniDto getAlumniById(Long alumniId) throws Exception {
        //TODO: check id
        //TODO: fetch Alumni from repository
        //TODO: map to AlumniDto
        return null;
    }

    @Override
    public List<AlumniDto> getAllAlumni() throws Exception {
        //TODO: fetch all Alumni from repository
        //TODO: map list to List<AlumniDto>
        return Collections.emptyList();
    }

    @Override
    public AlumniDto createAlumni(AlumniDto dto) throws Exception {
        //TODO: check for duplicate in db
        //TODO: map dto to Alumni
        //TODO: create AlumniDetails (can be optional or mandatory)
        //TODO: persist
        // TODO: map saved to dto
        return null;
    }

    @Override
    public AlumniDto updateAlumni(Long alumniId, AlumniDto dto) throws Exception {
        //TODO: check id
        //TODO: fetch Alumni or throw exception
        //TODO: map dto to Alumni and persist changes
        //TODO: map updated to dto
        return null;
    }

    @Override
    public AlumniDto deleteAlumni(Long alumniId) throws Exception {
        //TODO: check id
        //TODO: fetch Alumni or throw exception
        //TODO: remove associated details or delete on cascade
        //TODO: delete
        return null;
    }

    @Override
    public AlumniDetailsDto getAlumniDetails(Long alumniId) throws Exception {
        //TODO: check id
        //TODO: fetch AlumniDetails or throw exception
        //TODO: map to AlumniDetailsDto
        return null;
    }

    @Override
    public AlumniDetailsDto updateAlumniDetails(Long alumniId, AlumniDetailsDto dto) throws Exception {
        //TODO: check id
        //TODO: fetch AlumniDetails or throw exception
        //TODO: map dto to AlumniDetails and persist changes
        //TODO: map updated to dto
        return null;
    }
}
