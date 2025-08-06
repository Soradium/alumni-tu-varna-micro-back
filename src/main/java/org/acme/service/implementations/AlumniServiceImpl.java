package org.acme.service.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.acme.avro.back.AlumniDto;
import org.acme.avro.front.AlumniFrontDto;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;
import org.acme.repository.AlumniDetailsRepository;
import org.acme.repository.AlumniRepository;
import org.acme.service.AlumniService;
import org.acme.util.mappers.AlumniMapper;

import io.quarkus.panache.common.Sort;

public class AlumniServiceImpl implements AlumniService{

    private final AlumniRepository alumniRepository;
    private final AlumniDetailsRepository alumniDetailsRepository;
    private final AlumniMapper alumniMapper;


    @Override
    public Alumni getAlumniByFacultyNumber(long facultyNumber) throws Exception {
        Optional<Alumni> alumni = alumniRepository.findByIdOptional(facultyNumber);
        return alumni.orElseThrow(() -> new Exception("Not Found"));
    }

    @Override
    public List<Alumni> getAllAlumni() throws Exception {
        List<Alumni> alumniList = alumniRepository.findAll().list();
        return alumniList;
    }

    @Override
    public List<AlumniDto> getAllAlumniDto() throws Exception { // add pagination?
        List<AlumniDto> alumniListDtos = new ArrayList<>();
        List<Alumni> alumniList = alumniRepository.findAll(Sort.by("faculty_number")).list();
        List<AlumniDetails> alumniDetailsList = alumniDetailsRepository.findAll(Sort.by("faculty_number")).list();
        if(alumniList.isEmpty()) {
            return new ArrayList<AlumniDto>();
        }
        else {
            for(int i = 0; i < alumniList.size(); i++) {
                if(alumniList.get(i).getFacultyNumber() != alumniDetailsList.get(i).getFacultyNumber()) {
                    throw new Exception("Arrays are deformed!");
                }
                alumniListDtos.add(alumniMapper.toDto(alumniList.get(i), alumniDetailsList.get(i)));
            }
            return alumniListDtos;
        }
    }

    @Override
    public List<AlumniDto> getAlumniByDegree(String degree) throws Exception {
        alumniRepository.find("select", null)
        throw new UnsupportedOperationException("Unimplemented method 'getAlumniByDegree'");
    }

    @Override
    public Alumni saveAlumni(Alumni alumni) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAlumni'");
    }

    @Override
    public Alumni saveAlumni(AlumniFrontDto alumni) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAlumni'");
    }

    @Override
    public Alumni updateAlumni(Alumni alumni) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAlumni'");
    }

    @Override
    public Alumni updateAlumni(AlumniFrontDto alumni) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAlumni'");
    }

    @Override
    public void deleteAlumni(int facultyNumber) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAlumni'");
    }

    @Override
    public Alumni convertAlumniFromDto(AlumniFrontDto dto) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertAlumniFromDto'");
    }

    @Override
    public AlumniDto convertAlumniToDto(Alumni alumni) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertAlumniToDto'");
    }
    
}
