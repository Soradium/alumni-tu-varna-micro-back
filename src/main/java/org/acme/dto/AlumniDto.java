package org.acme.dto;

import org.acme.entites.AlumniGroupsMembership;

import java.util.ArrayList;

public class AlumniDto {
    private Integer facultyNumber;
    private String facebookUrl;
    private String linkedInUrl;
    private DegreeDto degree;
    private ArrayList<AlumniGroupsMembershipDto> memberships;

    public AlumniDto() {}

    public Integer getFacultyNumber() {
        return facultyNumber;
    }

    public void setFacultyNumber(Integer facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    public DegreeDto getDegreeDto() {
        return degree;
    }

    public void setDegreeDto(DegreeDto degree) {
        this.degree = degree;
    }

    public ArrayList<AlumniGroupsMembershipDto> getMemberships() {
        return memberships;
    }

    public void setMemberships(ArrayList<AlumniGroupsMembershipDto> memberships) {
        this.memberships = memberships;
    }
}
