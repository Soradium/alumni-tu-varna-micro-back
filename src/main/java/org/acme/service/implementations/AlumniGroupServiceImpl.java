package org.acme.service.implementations;

import java.util.List;

import org.acme.avro.ambiguous.AlumniGroupDtoSimplified;
import org.acme.avro.back.AlumniGroupBackDto;
import org.acme.entites.AlumniGroup;
import org.acme.repository.AlumniGroupRepository;
import org.acme.service.AlumniGroupCommonMapperService;
import org.acme.service.AlumniGroupService;
import org.acme.util.mappers.AlumniGroupMapper;
import org.acme.util.mappers.FacultyMapper;
import org.acme.util.mappers.SpecialityMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AlumniGroupServiceImpl implements AlumniGroupService {

    private final AlumniGroupRepository groupRepository;
    private final AlumniGroupMapper groupMapper;
    private final FacultyMapper facultyMapper;
    private final SpecialityMapper specialityMapper;
    private final AlumniGroupCommonMapperService commonMapper;

    @Inject
    public AlumniGroupServiceImpl(AlumniGroupRepository groupRepository, AlumniGroupMapper groupMapper,
                                  FacultyMapper facultyMapper, SpecialityMapper specialityMapper,
                                  AlumniGroupCommonMapperService commonMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.facultyMapper = facultyMapper;
        this.specialityMapper = specialityMapper;
        this.commonMapper = commonMapper;
    }

    @Override
    public AlumniGroup createAlumniGroup(AlumniGroupDtoSimplified dto) throws Exception {
        if (dto == null) {
            throw new NullPointerException();
        }
        AlumniGroup group = groupMapper.toEntitySimplified(dto);
        groupRepository.persist(group);
        return group;
    }

    @Override
    public AlumniGroup createAlumniGroup(AlumniGroup group) throws Exception {
        if (group == null) {
            throw new NullPointerException();
        }
        groupRepository.persist(group);
        return group;
    }

    @Override
    public void deleteAlumniGroup(Integer id) throws Exception {
        groupRepository.deleteById((long) id);
    }

    @Override
    public void deleteAlumniGroup(AlumniGroupDtoSimplified group) throws Exception {
        groupRepository.deleteById((long) group.getId());
    }

    @Override
    public void deleteAlumniGroup(AlumniGroup group) throws Exception {
        groupRepository.delete(group);
    }

    @Override
    public List<AlumniGroupBackDto> getAllAlumniGroupsDtoByFaculty(String faculty) throws Exception {
        return groupMapper.toDtoList(groupRepository.find("faculty.facultyName", faculty).list());
    }

    @Override
    public List<AlumniGroupBackDto> getAllAlumniGroupsDtoByGraduationYear(int graduationYear) throws Exception {
        return groupMapper.toDtoList(groupRepository.find("graduationYear", graduationYear).list());
    }

    @Override
    public List<AlumniGroupBackDto> getAllAlumniGroupsDtoBySpeciality(String speciality) throws Exception {
        return groupMapper.toDtoList(groupRepository.find("speciality.specialityName", speciality).list());
    }

    @Override
    public AlumniGroup getAlumniGroupById(Integer id) throws Exception {
        return groupRepository.findByIdOptional((long) id)
                .orElseThrow(() -> new Exception(
                        "No Alumni Group was found."));

    }

    @Override
    public AlumniGroup updateAlumniGroup(AlumniGroupDtoSimplified dto) throws Exception {
        AlumniGroup existing = groupRepository.findByIdOptional((long) dto
                .getId()).orElseThrow(() -> new Exception(
                "AlumniGroup with such ID does not exist."));
        existing.setFaculty(facultyMapper.toEntity(dto.getFaculty()));
        existing.setGraduationYear(dto.getGraduationYear());
        existing.setGroupNumber(dto.getGroupNumber());
        existing.setSpeciality(specialityMapper
                .toEntity(dto.getSpeciality()));
        groupRepository.persist(existing);
        return existing;
    }

    @Override
    public AlumniGroup updateAlumniGroup(AlumniGroup group) throws Exception {
        AlumniGroup existing = groupRepository.findByIdOptional((long) group.getId()).orElseThrow(() -> new Exception("AlumniGroup with such ID does not exist."));
        existing.setFaculty(group.getFaculty());
        existing.setGraduationYear(group.getGraduationYear());
        existing.setGroupNumber(group.getGroupNumber());
        existing.setSpeciality(group.getSpeciality());
        existing.setMemberships(group.getMemberships());

        groupRepository.persist(existing);
        return existing;
    }

    @Override
    public AlumniGroupBackDto getAlumniGroupDtoById(Integer id) throws Exception {
        return commonMapper.toAlumniGroupDto(groupRepository.findByIdOptional((long) id)
                .orElseThrow(() -> new Exception(
                        "No Alumni Group was found.")));
    }

}
