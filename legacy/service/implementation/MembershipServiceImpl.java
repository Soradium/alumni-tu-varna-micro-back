package org.acme.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.AlumniGroupsMembershipDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.AlumniGroupsMembershipRepository;
import org.acme.service.AlumniGroupService;
import org.acme.service.AlumniGroupsMembershipService;
import org.acme.service.AlumniService;
import org.acme.util.mappers.AlumniGroupsMembershipMapper;

import java.util.List;

@ApplicationScoped
public class MembershipServiceImpl implements AlumniGroupsMembershipService {
    @Inject
    AlumniGroupsMembershipRepository repository;
    @Inject
    AlumniService alumniService;
    @Inject
    AlumniGroupService alumniGroupService;
    @Inject
    AlumniGroupsMembershipMapper mapper;

    @Override
    public AlumniGroupsMembershipDto getMembershipByFN(int id) {
        AlumniGroupsMembership membership = repository.findByFNOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found"));
        return mapper.toDto(membership);
    }

    @Override
    public List<AlumniGroupsMembershipDto> getAllMemberships() {
        List<AlumniGroupsMembership> list = repository.listAll();
        if(list.isEmpty()){
            throw new ResourceNotFoundException("Membership not found");
        }
        return list.stream().map(mapper::toDto).toList();
    }

    @Override
    @Transactional
    public AlumniGroupsMembershipDto createMembership(AlumniGroupsMembershipDto dto) throws Exception {
        Alumni a = alumniService.getAlumniByIdE(dto.getFacultyNumber());
        AlumniGroup group = alumniGroupService.getAlumniGroupByGroupNumberE(dto.getGroupNumber());
        AlumniGroupsMembership entity = mapper.fromDto(dto, a, group);
        repository.persist(entity);
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public AlumniGroupsMembershipDto updateMembership(int id, AlumniGroupsMembershipDto dto) {
        AlumniGroupsMembership db = repository.findByFNOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found"));
        db.setAverageScore(dto.getAverageScore());
        repository.persist(db);
        return mapper.toDto(db);
    }

    @Override
    @Transactional
    public AlumniGroupsMembershipDto deleteMembership(int id) {
        AlumniGroupsMembership db = repository.findByFNOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found"));
        repository.delete(db);
        return mapper.toDto(db);
    }
}
