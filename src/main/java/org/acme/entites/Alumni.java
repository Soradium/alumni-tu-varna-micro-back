package org.acme.entites;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "alumni")
public class Alumni extends PanacheEntityBase {

    @Id
    @Column(name = "faculty_number")
    private Integer facultyNumber;
    @OneToMany(mappedBy = "alumni")
    public List<AlumniGroupsMembership> memberships;
    @Column(name = "facebook_url")
    private String facebookUrl;
    @Column(name = "linkedin_url")
    private String linkedInUrl;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "degree_id")
    private Degree degree;

    public Alumni() {
    }

    public List<AlumniGroupsMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<AlumniGroupsMembership> memberships) {
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
