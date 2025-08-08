package org.acme.service.implementations;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;
import org.acme.repository.AlumniDetailsRepository;
import org.acme.repository.AlumniRepository;
import org.acme.repository.DegreeRepository;
import org.acme.service.AlumniService;
import org.acme.util.mappers.AlumniMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AlumniServiceImpl implements AlumniService {

    private final AlumniRepository alumniRepository;
    private final AlumniMapper alumniMapper;
    private final AlumniDetailsRepository alumniDetailsRepository;
    private final DegreeRepository degreeRepository;


    @Inject
    public AlumniServiceImpl(AlumniRepository alumniRepository, AlumniMapper alumniMapper,
                             AlumniDetailsRepository alumniDetailsRepository, DegreeRepository degreeRepository) {
        this.alumniRepository = alumniRepository;
        this.alumniMapper = alumniMapper;
        this.alumniDetailsRepository = alumniDetailsRepository;
        this.degreeRepository = degreeRepository;
    }

    @Override
    public Alumni getAlumniByFacultyNumber(long facultyNumber) throws Exception {
        Optional<Alumni> alumni = alumniRepository.findByIdOptional(facultyNumber);
        return alumni.orElseThrow(() -> new Exception("Alumni not found."));
    }

    @Override
    public AlumniDto getAlumniDtoByFacultyNumber(long facultyNumber) throws Exception {
        return alumniMapper.toDto(
                getAlumniByFacultyNumber(facultyNumber),
                alumniDetailsRepository.findByIdOptional(
                                facultyNumber)
                        .orElseThrow(() -> new Exception(
                                "Alumni Details not found.")));
    }

    @Override
    public List<Alumni> getAllAlumni() throws Exception {
        List<Alumni> alumniList = alumniRepository.findAll().list();
        return alumniList;
    }

    @Override
    public List<AlumniDto> getAllAlumniDto() throws Exception { // add pagination?
        List<Alumni> alumniList = alumniRepository.findAll(Sort.by("faculty_number")).list();
        List<AlumniDetails> alumniDetailsList = alumniDetailsRepository.findAll(Sort.by("faculty_number")).list();
        if (alumniList.isEmpty() || alumniDetailsList.isEmpty()) {
            throw new Exception("AlumniLists were not populated properly.");
        } else {
            return convertAlumniListToDtoList(alumniList, alumniDetailsList);
        }
    }

    @Override
    public List<AlumniDto> getAlumniByDegree(String degree) throws Exception {
        if (degree == null) {
            throw new Exception("No degree was passed.");
        }
        List<Alumni> alumni = alumniRepository.findAllAlumniByDegree(degree);
        return convertAlumniListToDtoList(
                alumni,
                alumniDetailsRepository.findAllAlumniDetailsForAllAlumni(alumni));
    }


    @Override
    public Alumni saveAlumni(Alumni alumni, AlumniDetails details) throws Exception {
        alumniDetailsRepository.persist(details);
        alumniRepository.persist(alumni);
        return alumni;
    }

    @Override
    public Alumni saveAlumni(AlumniFrontDto alumni) throws Exception {
        Alumni a = alumniMapper.toAlumniEntityFront(alumni);
        alumniDetailsRepository.persist(alumniMapper.toAlumniDetailsEntityFront(alumni));
        alumniRepository.persist(a);
        return a;
    }

    @Override
    public Alumni updateAlumni(Alumni alumni) throws Exception {
        if (alumni == null || alumni.getFacultyNumber() == null) {
            throw new IllegalArgumentException("Alumni or faculty number must not be null");
        }
        Alumni existingAlumni = Alumni.findById(alumni.getFacultyNumber());

        if (existingAlumni == null) {
            throw new Exception("Alumni with faculty number " + alumni.getFacultyNumber() + " not found");
        }

        existingAlumni.setFacebookUrl(alumni.getFacebookUrl());
        existingAlumni.setLinkedInUrl(alumni.getLinkedInUrl());

        if (alumni.getDegree() != null) {
            existingAlumni.setDegree(alumni.getDegree());
        }

        existingAlumni.persist();

        return existingAlumni;
    }


    @Override
    public Alumni updateAlumni(AlumniFrontDto alumni) throws Exception {
        if (alumni == null || alumni.getFacultyNumber() == 0) {
            throw new IllegalArgumentException("Alumni or faculty number must not be null");
        }
        Alumni existingAlumni = Alumni.findById(alumni.getFacultyNumber());

        if (existingAlumni == null) {
            throw new Exception("Alumni with faculty number " + alumni.getFacultyNumber() + " not found");
        }

        existingAlumni.setFacebookUrl(alumni.getFacebookUrl());
        existingAlumni.setLinkedInUrl(alumni.getLinkedinUrl());


        if (alumni.getDegree() != null) {
            existingAlumni.setDegree(degreeRepository
                    .findByNameOptional(alumni.getDegree())
                    .orElseThrow(() -> new Exception(
                            "Degree passed by AlumniFrontDto does not exist."))
            );
        }

        existingAlumni.persist();

        return existingAlumni;
    }

    @Override
    public void deleteAlumni(int facultyNumber) throws Exception {
        alumniDetailsRepository.delete(alumniDetailsRepository.findById((long) facultyNumber));
        alumniRepository.delete(alumniRepository.findById((long) facultyNumber));
    }

    public List<AlumniDto> convertAlumniListToDtoList(
            List<Alumni> alumniList,
            List<AlumniDetails> alumniDetailsList) throws Exception {
        List<AlumniDto> alumniListDtos = new ArrayList<>();
        if (alumniList == null
                || alumniList.isEmpty()
                || alumniDetailsList == null
                || alumniDetailsList.isEmpty()) {
            return alumniListDtos;
        }
        for (int i = 0; i < alumniList.size(); i++) {
            if (alumniList.get(i).getFacultyNumber() != alumniDetailsList.get(i).getFacultyNumber()) {
                throw new Exception("Arrays are deformed!");
            }
            alumniListDtos.add(alumniMapper.toDto(alumniList.get(i), alumniDetailsList.get(i)));
        }
        return alumniListDtos;
    }

}
