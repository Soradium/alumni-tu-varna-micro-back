package org.acme.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

@Entity
@Table(name = "alumni_groups")
public class AlumniGroup extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groups_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @NotNull
    @NotBlank
    @Column(name = "group_number")
    private int groupNumber;

    @NotNull
    @NotBlank
    @Column(name = "graduation_year")
    private int graduationYear;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "speciality_id")
    private Speciality speciality;

    @OneToMany(mappedBy = "group_number")
    public ArrayList<AlumniGroupsMembership> memberships;

    public AlumniGroup() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Faculty getFaculties() {
        return faculty;
    }

    public void setFaculties(Faculty faculty) {
        this.faculty = faculty;
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

    public Speciality getSpecialities() {
        return speciality;
    }

    public void setSpecialities(Speciality speciality) {
        this.speciality = speciality;
    }

    public ArrayList<AlumniGroupsMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(ArrayList<AlumniGroupsMembership> memberships) {
        this.memberships = memberships;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    
}
