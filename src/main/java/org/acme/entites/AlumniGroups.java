package org.acme.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

@Entity
@Table(name = "alumni_groups")
public class AlumniGroups extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculties faculties;

    @NotNull
    @NotBlank
    private int groupNumber;

    @NotNull
    @NotBlank
    private int graduationYear;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "speciality_id")
    private Specialities specialities;

    @OneToMany(mappedBy = "group")
    public ArrayList<AlumniGroupsMembership> memberships;

    public AlumniGroups() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Faculties getFaculties() {
        return faculties;
    }

    public void setFaculties(Faculties faculties) {
        this.faculties = faculties;
    }

    @NotNull
    @NotBlank
    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(@NotNull @NotBlank int groupNumber) {
        this.groupNumber = groupNumber;
    }

    @NotNull
    @NotBlank
    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(@NotNull @NotBlank int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public Specialities getSpecialities() {
        return specialities;
    }

    public void setSpecialities(Specialities specialities) {
        this.specialities = specialities;
    }

    public ArrayList<AlumniGroupsMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(ArrayList<AlumniGroupsMembership> memberships) {
        this.memberships = memberships;
    }
}
