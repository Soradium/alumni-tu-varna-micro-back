package org.acme.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.avro.back.AlumniDto;
import org.acme.avro.back.AlumniGroupMembershipDto;
import org.acme.entites.*;
import org.acme.exceptions.IncorrectAlumnusNumberException;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.AlumniDetailsRepository;
import org.acme.repository.AlumniRepository;
import org.acme.service.AlumniGroupService;
import org.acme.service.AlumniService;
import org.acme.service.DegreeService;
import org.acme.service.FacultyService;
import org.acme.util.mappers.AlumniGroupMembershipMapper;
import org.acme.util.mappers.AlumniMapper;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AlumniServiceImpl implements AlumniService {

    private final AlumniRepository alumniRepository;
    private final AlumniGroupService groupService;
    private final AlumniDetailsRepository alumniDetailsRepository;
    private final DegreeService degreeService;
    private final FacultyService facultyService;
    private final AlumniMapper alumniMapper;
    private final AlumniGroupMembershipMapper membershipMapper;

    
    @Inject
    public AlumniServiceImpl(AlumniRepository alumniRepository, AlumniGroupService groupService,
            AlumniDetailsRepository alumniDetailsRepository, DegreeService degreeService, FacultyService facultyService,
            AlumniMapper alumniMapper, AlumniGroupMembershipMapper membershipMapper) {
        this.alumniRepository = alumniRepository;
        this.groupService = groupService;
        this.alumniDetailsRepository = alumniDetailsRepository;
        this.degreeService = degreeService;
        this.facultyService = facultyService;
        this.alumniMapper = alumniMapper;
        this.membershipMapper = membershipMapper;
    }



    @Override
    public AlumniDto getAlumniById(int alumniId) throws Exception {
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
        Alumni alumni = alumniRepository.findByIdOptional((long) alumniId).orElseThrow(
                () -> new ResourceNotFoundException("Alumni with id " + alumniId + " not found")
        );
        return alumniMapper.toDto(alumni);
    }

    

    @Override
    public AlumniDetails getAlumniDetailsByIdE(int alumniId) throws Exception {
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
        AlumniDetails details = alumniDetailsRepository.findByIdOptional((long) alumniId).orElseThrow(
            () -> new ResourceNotFoundException("Alumni with id " + alumniId + " not found");
        );
        return details;
    }



    @Override
    public Alumni getAlumniByIdE(int alumniId) throws Exception {
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
        Alumni alumni = alumniRepository.findByIdOptional((long) alumniId).orElseThrow(
                () -> new ResourceNotFoundException("Alumni with id " + alumniId + " not found")
        );
        return alumni;
    }

    @Override
    public List<AlumniDto> getAllAlumni() throws Exception {
        List<Alumni> dbList = alumniRepository.listAll();
        if(dbList.isEmpty())
            throw new ResourceNotFoundException("No alumni found");
        return dbList.stream().map(alumniMapper::toDto).toList();
    }

    @Override
    @Transactional
    public AlumniDto createAlumni(AlumniDto dto) {
        Degree d = degreeService.getDegreeByNameE(dto.getDegreeDto().getDegree());
        Alumni newAlumni = alumniMapper.toEntity(dto, d);

        if (dto.getMemberships() != null) {
            ArrayList<AlumniGroupsMembership> memberships = new ArrayList<>();
            for (AlumniGroupsMembershipDto mDto : dto.getMemberships()) {
                AlumniGroup group = groupService.getAlumniGroupByGroupNumberE(mDto.getGroupNumber());
                AlumniGroupsMembership membership = membershipMapper.fromDto(mDto, newAlumni, group);
                memberships.add(membership);
            }
        newAlumni.setMemberships(memberships);
        }
        alumniRepository.persist(newAlumni);
        return alumniMapper.toDto(newAlumni);
    }

    @Override
    @Transactional
    public AlumniDto updateAlumni(int alumniId, AlumniDto dto) throws Exception {
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
        Alumni alumni = alumniRepository.findByIdOptional((long) alumniId).orElseThrow(
                () -> new ResourceNotFoundException("Alumni with id " + alumniId + " not found")
        );
        Degree d = degreeService.getDegreeByNameE(dto.getDegreeDto().getDegree());
        alumniMapper.updateEntity(dto, alumni, d);

        //update, not replace?
        if (dto.getMemberships() != null) {
            ArrayList<AlumniGroupsMembership> memberships = new ArrayList<>();
            for (AlumniGroupsMembershipDto mDto : dto.getMemberships()) {
                AlumniGroup group = groupService.getAlumniGroupByGroupNumberE(mDto.getGroupNumber());
                AlumniGroupsMembership membership = membershipMapper.fromDto(mDto, alumni, group);
                memberships.add(membership);
            }
            alumni.setMemberships(memberships);
        }
        alumniRepository.persist(alumni);
        return alumniMapper.toDto(alumni);
    }

    @Override
    @Transactional
    public AlumniDto deleteAlumni(int alumniId) throws Exception {
        if(alumniId <= 0)
            throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
        Alumni alumni = alumniRepository.findByIdOptional((long) alumniId).orElseThrow(
                () -> new ResourceNotFoundException("Alumni with id " + alumniId + " not found")
        );
        alumniRepository.delete(alumni);
        return alumniMapper.toDto(alumni);
    }

    @Override
    @Transactional
    public AlumniDto createAlumniDetails(AlumniDto dto) throws Exception {
        Faculty f = facultyService.getFacultyByNameE(dto.getFaculty());
        AlumniDetails newDetails = alumniMapper.toDetailsEntity(dto, f);
        alumniDetailsRepository.persist(newDetails);
        return alumniMapper.toDetailsDto(newDetails);
    }

    // @Override
    // @Transactional
    // public AlumniDetailsDto createAlumniDetails(AlumniDetailsDto dto) throws Exception {
    //     Faculty f = facultyService.getFacultyByIdE(dto.getFaculty());
    //     AlumniDetails newDetails = alumniMapper.toDetailsEntity(dto, f);
    //     alumniDetailsRepository.persist(newDetails);
    //     return alumniMapper.toDetailsDto(newDetails);
    // }

    // @Override
    // public AlumniDetailsDto getAlumniDetails(int alumniId) throws Exception {
    //     if(alumniId <= 0)
    //         throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
    //     AlumniDetails details = alumniDetailsRepository.findByIdOptional((long) alumniId)
    //             .orElseThrow(() -> new ResourceNotFoundException("Details for alumnus with id "
    //                     + alumniId + " not found"));
    //     return alumniMapper.toDetailsDto(details);
    // }

    // @Override
    // @Transactional
    // public AlumniDetailsDto updateAlumniDetails(int alumniId, AlumniDetailsDto dto) throws Exception {
    //     if(alumniId <= 0)
    //         throw new IncorrectAlumnusNumberException("Alumnus id value must be positive");
    //     AlumniDetails details = alumniDetailsRepository.findByIdOptional((long) alumniId)
    //             .orElseThrow(() -> new ResourceNotFoundException("Details for alumnus with id "
    //                     + alumniId + " not found"));
    //     Faculty f = facultyService.getFacultyByIdE(dto.getFaculty());
    //     alumniMapper.updateDetails(dto, details, f);
    //     alumniDetailsRepository.persist(details);
    //     return alumniMapper.toDetailsDto(details);
    // }
}
