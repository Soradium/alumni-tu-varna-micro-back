package org.acme.dto;

import org.acme.entites.AlumniGroupsMembership;
import org.acme.entites.Faculty;
import org.acme.entites.Speciality;

import java.util.ArrayList;

public class AlumniGroupDto {

    private Integer id;
    private FacultyDto faculty;
    private int groupNumber;
    private int graduationYear;
    private SpecialityDto speciality;
    private ArrayList<AlumniGroupsMembershipDto> memberships;

    public AlumniGroupDto() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FacultyDto getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyDto faculty) {
        this.faculty = faculty;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public SpecialityDto getSpeciality() {
        return speciality;
    }

    public void setSpeciality(SpecialityDto speciality) {
        this.speciality = speciality;
    }

    public ArrayList<AlumniGroupsMembershipDto> getMemberships() {
        return memberships;
    }

    public void setMemberships(ArrayList<AlumniGroupsMembershipDto> memberships) {
        this.memberships = memberships;
    }
}
