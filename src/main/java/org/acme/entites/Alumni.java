package org.acme.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name = "alumni")
public class Alumni extends PanacheEntityBase {

    @OneToMany(mappedBy = "alumni")
    public ArrayList<AlumniGroupsMembership> memberships;
    @Id
    @Column(name = "faculty_number")
    private Integer facultyNumber;
    @Column(name = "facebook_url")
    private String facebookUrl;
    @Column(name = "linkedin_url")
    private String linkedInUrl;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "degree_id")
    private Degree degree;

    public Alumni() {
    }

    public ArrayList<AlumniGroupsMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(ArrayList<AlumniGroupsMembership> memberships) {
        this.memberships = memberships;
    }

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

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }


}
