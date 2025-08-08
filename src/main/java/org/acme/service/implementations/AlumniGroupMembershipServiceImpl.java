package org.acme.service.implementations;

import java.util.List;

import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.avro.front.AlumniGroupMembershipFrontDto;
import org.acme.entites.AlumniGroupsMembership;
import org.acme.repository.AlumniGroupRepository;
import org.acme.repository.AlumniGroupsMembershipRepository;
import org.acme.repository.AlumniRepository;
import org.acme.service.AlumniGroupMembershipService;
import org.acme.util.mappers.AlumniGroupMembershipMapper;
import org.acme.util.mappers.AlumniMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
@ApplicationScoped
public class AlumniGroupMembershipServiceImpl implements AlumniGroupMembershipService {

    private final AlumniGroupsMembershipRepository membershipRepository;
    private final AlumniRepository alumniRepository;
    private final AlumniGroupRepository groupRepository;
    private final AlumniGroupMembershipMapper groupMembershipMapper;
    private final AlumniMapper alumniMapper;

    
    @Inject
    public AlumniGroupMembershipServiceImpl(AlumniGroupsMembershipRepository membershipRepository,
            AlumniRepository alumniRepository, AlumniGroupRepository groupRepository,
            AlumniGroupMembershipMapper groupMembershipMapper,
            AlumniMapper alumniMapper) {
        this.membershipRepository = membershipRepository;
        this.alumniRepository = alumniRepository;
        this.groupRepository = groupRepository;
        this.groupMembershipMapper = groupMembershipMapper;
        this.alumniMapper = alumniMapper;
    }

    @Override
    public AlumniGroupsMembership createAlumniGroupsMembership(AlumniGroupMembershipFrontDto dto) throws Exception {
        if (dto == null) {
            throw new Exception("Membership DTO is null.");
        }

        AlumniGroupsMembership membership = groupMembershipMapper.toEntityFront(dto);

        membership.setGroup(groupRepository
            .findByIdOptional((long) dto.getGroupNumber())
            .orElseThrow(() -> new Exception(
                "AlumniGroup not found."))
                );
        membership.setAlumni(alumniRepository
            .findByIdOptional((long) dto.getFacultyNumber())
            .orElseThrow(() -> new Exception(String.format(
                "Alumni with faculty number %d was not found", 
                dto.getFacultyNumber())))
                );
        membership.setAverageScore(dto.getAverageScore());

        membershipRepository.persist(membership);
        return membership;
    }

    @Override
    public AlumniGroupsMembership createAlumniGroupsMembership(AlumniGroupsMembership membership) throws Exception {
        if (membership == null) {
            throw new Exception("Membership entity is null.");
        }

        membershipRepository.persist(membership);
        return membership;
    }

    @Override
    public void deleteAlumniGroupsMembership(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Invalid membership ID.");
        }

        boolean deleted = membershipRepository.deleteById((long)id);
        if (!deleted) {
            throw new Exception("Membership with ID " + id + " not found.");
        }
    }

    @Override
    public void deleteAlumniGroupsMembership(AlumniGroupMembershipFrontDto dto) throws Exception {
        if (dto == null) {
            throw new Exception("DTO is null.");
        }
        deleteAlumniGroupsMembership(dto.getId());
    }

    @Override
    public void deleteAlumniGroupsMembership(AlumniGroupsMembership membership) throws Exception {
        if (membership == null || membership.getId() == null) {
            throw new Exception("Invalid membership.");
        }
        deleteAlumniGroupsMembership(membership.getId());
    }

    @Override
    public List<AlumniGroupMembershipDto> getAllAlumniGroupMembershipsByFacultyNumber(int facultyNumber) throws Exception {
        List<AlumniGroupsMembership> memberships =
            membershipRepository.findAllByFacultyNumber(facultyNumber);
        return groupMembershipMapper.toBackDtos(memberships);
    }

    @Override
    public List<AlumniGroupMembershipDto> getAllAlumniGroupMembershipsByGroup(int groupId) throws Exception {
        List<AlumniGroupsMembership> memberships = membershipRepository.findAllByGroupId(groupId);
        return groupMembershipMapper.toBackDtos(memberships);
    }

    @Override
    public List<AlumniGroupMembershipDto> getAllAlumniGroupMembershipsDto() throws Exception {
        List<AlumniGroupsMembership> memberships = membershipRepository.findAll().list();
        return groupMembershipMapper.toBackDtos(memberships);
    }

    @Override
    public AlumniGroupsMembership getAlumniGroupsMembershipById(Integer id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("Invalid ID.");
        }

        AlumniGroupsMembership membership = membershipRepository.findById((long) id);
        if (membership == null) {
            throw new Exception("Membership not found.");
        }
        return membership;
    }

    @Override
    public AlumniGroupsMembership getAlumniGroupsMembershipByName(String name) throws Exception {
        // If you don't have a name field, this method may not be needed
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public AlumniGroupsMembership updateAlumniGroupsMembership(AlumniGroupMembershipFrontDto dto) throws Exception {
        if (dto == null || dto.getId() <= 0) {
            throw new Exception("Invalid DTO.");
        }

        AlumniGroupsMembership existing = membershipRepository
            .findByIdOptional((long)dto.getId())
            .orElseThrow(() -> new Exception(
                "No such group membership was found.")
                );

        existing.setAlumni(alumniRepository
            .findByIdOptional((long) dto.getFacultyNumber())
            .orElseThrow(() -> new Exception(
                "No such alumni was found."))
                );
        existing.setAverageScore(dto.getAverageScore());
        existing.setGroup(groupRepository
            .findByIdOptional((long) dto.getGroupNumber())
            .orElseThrow(() -> new Exception("No group with such id number was found.")));

        membershipRepository.persist(existing);
        return existing;
    }

    @Override
    public AlumniGroupsMembership updateAlumniGroupsMembership(AlumniGroupsMembership membership) throws Exception {
        if (membership == null || membership.getId() == null) {
            throw new Exception("Invalid membership.");
        }

        AlumniGroupsMembership existing = membershipRepository
            .findByIdOptional((long) membership.getId())
            .orElseThrow(() -> new Exception(
                "No such group membership was found.")
                );

        existing.setAverageScore(membership.getAverageScore());
        existing.setAlumni(membership.getAlumni());
        existing.setAverageScore(membership.getAverageScore());

        membershipRepository.persist(existing);
        return existing;
    }
}

