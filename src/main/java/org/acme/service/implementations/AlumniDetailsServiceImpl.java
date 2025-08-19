package org.acme.service.implementations;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;
import org.acme.repository.AlumniDetailsRepository;
import org.acme.repository.AlumniRepository;
import org.acme.repository.FacultyRepository;
import org.acme.service.AlumniDetailsService;
import org.acme.util.mappers.AlumniMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AlumniDetailsServiceImpl implements AlumniDetailsService {

    private final AlumniDetailsRepository detailsRepository;
    private final AlumniRepository alumniRepository;
    private final FacultyRepository facultyRepository;
    private final AlumniMapper alumniMapper;

    @Inject
    public AlumniDetailsServiceImpl(AlumniDetailsRepository detailsRepository, AlumniRepository alumniRepository,
                                    FacultyRepository facultyRepository, AlumniMapper alumniMapper) {
        this.detailsRepository = detailsRepository;
        this.alumniRepository = alumniRepository;
        this.facultyRepository = facultyRepository;
        this.alumniMapper = alumniMapper;
    }

    @Override
    public List<AlumniDetails> getAllAlumniDetails() throws Exception {
        return detailsRepository.listAll();
    }

    @Override
    public List<AlumniDto> getAlumniListByFaculty(String facultyName) throws Exception {
        List<AlumniDetails> details = detailsRepository
                .find("faculty.facultyName", facultyName)
                .list();
        List<Alumni> alumniList = new ArrayList<>();
        alumniList = details.stream().map(alumniDetail -> {
            return alumniRepository
                    .findByIdOptional((long) alumniDetail.getFacultyNumber())
                    .orElseThrow(() -> new RuntimeException(
                            "ID mismatch. ID related to Details does not exist in Alumni Table.")
                    );
        }).toList();

        List<AlumniDto> dtoList = new ArrayList<>();
        for (int i = 0; i < details.size(); i++) {

            dtoList.add(alumniMapper.toDto(alumniList.get(i), details.get(i)));
        }
        return dtoList;

    }

    @Override
    public List<AlumniDto> getAlumniListByFullName(String fullName) throws Exception {
        // they share FN, so just take all alumnidetes that store the name
        // and then go across the fns and return them with their detes.
        List<AlumniDetails> details = detailsRepository
                .find("fullName", fullName)
                .list();
        List<Alumni> alumniList = new ArrayList<>();
        alumniList = details.stream().map(alumniDetail -> {
            return alumniRepository
                    .findByIdOptional((long) alumniDetail.getFacultyNumber())
                    .orElseThrow(() -> new RuntimeException(
                            "ID mismatch. ID related to Details does not exist in Alumni Table.")
                    );
        }).collect(Collectors.toList());

        List<AlumniDto> dtoList = new ArrayList<>();
        for (int i = 0; i < details.size(); i++) {

            dtoList.add(alumniMapper.toDto(alumniList.get(i), details.get(i)));
        }
        return dtoList;
    }

    @Override
    public AlumniDetails getAlumniDetailsByFacultyNumber(int facultyNumber) throws Exception {
        return detailsRepository
                .findByIdOptional((long) facultyNumber)
                .orElseThrow(() -> new Exception(
                        "No details related to this FN exist."));
    }

    @Override
    public AlumniDetails getDetailsForAlumni(Alumni alumni) throws Exception {
        return detailsRepository
                .findByIdOptional((long) alumni.getFacultyNumber())
                .orElseThrow(() -> new Exception(
                        "No details related to this FN exist."));

    }

    @Override
    public AlumniDetails getDetailsForAlumniDto(AlumniDto alumni) throws Exception {
        return detailsRepository
                .findByIdOptional((long) alumni.getFacultyNumber())
                .orElseThrow(() -> new Exception(
                        "No details related to this FN exist."));

    }

    @Override
    public List<AlumniDetails> getDetailsForListOfAlumni(List<Alumni> alumniList) throws Exception {
        return alumniList.stream().map(alumni -> {
            return detailsRepository
                    .findByIdOptional((long) alumni.getFacultyNumber())
                    .orElseThrow(() -> new RuntimeException(
                            "No details related to this FN exist."));
        }).collect(Collectors.toList());
    }

    @Override
    public AlumniDetails updateAlumniDetails(AlumniDetails alumniDetails) throws Exception {
        AlumniDetails existing =
                detailsRepository
                        .findByIdOptional((long) alumniDetails
                                .getFacultyNumber())
                        .orElseThrow(() -> new Exception(
                                "AlumniDetails with passed faculty number does not exist.")
                        );
        existing.setBirthDate(alumniDetails.getBirthDate());
        existing.setFaculty(alumniDetails.getFaculty());
        existing.setFullName(alumniDetails.getFullName());
        existing.setUpdatedAt(Timestamp.from(Instant.now()));

        detailsRepository.persist(existing);
        return existing;
    }

    @Override
    public AlumniDetails updateAlumniDetails(AlumniFrontDto alumniDetails) throws Exception {
        AlumniDetails existing =
                detailsRepository
                        .findByIdOptional((long) alumniDetails
                                .getFacultyNumber())
                        .orElseThrow(() -> new Exception(
                                "AlumniDetails with passed faculty number does not exist.")
                        );
        existing.setBirthDate(alumniDetails.getBirthDate());
        existing.setFaculty(facultyRepository
                .findByName(alumniDetails.getFaculty()));
        existing.setFullName(alumniDetails.getFullName());
        existing.setUpdatedAt(Timestamp.from(Instant.now()));

        detailsRepository.persist(existing);
        return existing;
    }


}
