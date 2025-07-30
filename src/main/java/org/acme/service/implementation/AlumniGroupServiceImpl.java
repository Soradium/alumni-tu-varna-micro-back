package org.acme.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.AlumniGroupDto;
import org.acme.dto.AlumniGroupsMembershipDto;
import org.acme.entites.*;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.AlumniGroupRepository;
import org.acme.service.AlumniGroupService;
import org.acme.service.AlumniService;
import org.acme.service.FacultyService;
import org.acme.service.SpecialityService;
import org.acme.util.mappers.AlumniGroupMapper;
import org.acme.util.mappers.AlumniGroupsMembershipMapper;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AlumniGroupServiceImpl implements AlumniGroupService {
    @Inject
    AlumniGroupRepository alumniGroupRepository;
    @Inject
    SpecialityService specialityService;
    @Inject
    FacultyService facultyService;
    @Inject
    AlumniService alumniService;
    @Inject
    AlumniGroupMapper groupMapper;
    @Inject
    AlumniGroupsMembershipMapper membershipMapper;

    @Override
    public AlumniGroupDto getAlumniGroupByGroupNumber(int groupNumber) {
        AlumniGroup alumniGroup = alumniGroupRepository.findByGroupNumberOptional(groupNumber)
                .orElseThrow(() -> new ResourceNotFoundException("AlumniGroup not found"));
        return groupMapper.toDto(alumniGroup);
    }

    @Override
    public AlumniGroup getAlumniGroupByGroupNumberE(int groupNumber) {
        AlumniGroup alumniGroup = alumniGroupRepository.findByGroupNumberOptional(groupNumber)
                .orElseThrow(() -> new ResourceNotFoundException("AlumniGroup not found"));
        return alumniGroup;
    }

    @Override
    @Transactional
    public AlumniGroupDto createAlumniGroup(AlumniGroupDto dto) throws Exception {
        Faculty f = facultyService
                .getFacultyByNameE(dto.getFaculty().getFacultyName());
        Speciality s = specialityService
                .getSpecialityByNameE(dto.getSpeciality().getSpeciality());
        List<AlumniGroupsMembership> memberships = new ArrayList<>();
        for (AlumniGroupsMembershipDto mDto : dto.getMemberships()) {
            Alumni alumni = alumniService.getAlumniByIdE(mDto.getFacultyNumber());
            memberships.add(membershipMapper.fromDto(mDto, alumni, null));
        }
        AlumniGroup alumniGroup = groupMapper.toEntity(dto, f, s, memberships);
        memberships.forEach(m -> m.setGroup(alumniGroup));
        alumniGroupRepository.persist(alumniGroup);
        return  groupMapper.toDto(alumniGroup);
    }

    @Override
    @Transactional
    public AlumniGroupDto updateAlumniGroup(int groupNumber, AlumniGroupDto dto) throws Exception {
        AlumniGroup  existingGroup = alumniGroupRepository.findByGroupNumberOptional(groupNumber)
                .orElseThrow(() -> new ResourceNotFoundException("AlumniGroup not found"));
        Faculty faculty = facultyService.getFacultyByNameE(dto.getFaculty().getFacultyName());
        Speciality speciality = specialityService.getSpecialityByNameE(dto.getSpeciality().getSpeciality());

        existingGroup.setFaculty(faculty);
        existingGroup.setSpeciality(speciality);
        List<AlumniGroupsMembership> updatedMemberships = new ArrayList<>();
        for (AlumniGroupsMembershipDto mDto : dto.getMemberships()) {
            Alumni alumni = alumniService.getAlumniByIdE(mDto.getFacultyNumber());
            AlumniGroupsMembership membership = membershipMapper.fromDto(mDto, alumni, existingGroup);
            updatedMemberships.add(membership);
        }
        existingGroup.getMemberships().clear();
        existingGroup.getMemberships().addAll(updatedMemberships);

        alumniGroupRepository.persist(existingGroup);
        return groupMapper.toDto(existingGroup);
    }

    @Override
    public AlumniGroupDto assignToGroup(AlumniGroupsMembershipDto dto) throws Exception {
        Alumni alumni = alumniService.getAlumniByIdE(dto.getFacultyNumber());
        AlumniGroup group = alumniGroupRepository.findByGroupNumberOptional(dto.getGroupNumber())
                .orElseThrow(() -> new ResourceNotFoundException("AlumniGroup not found"));

        boolean alreadyMember = group.getMemberships().stream()
                .anyMatch(m -> m.getAlumni().getFacultyNumber().equals(dto.getFacultyNumber()));
        if (alreadyMember) {
            throw new IllegalStateException("Alumni is already a member of this group.");
        }

        AlumniGroupsMembership membership = membershipMapper.fromDto(dto, alumni, group);
        group.getMemberships().add(membership);
        alumniGroupRepository.persist(group);
        return groupMapper.toDto(group);
    }
}
