package org.acme.entites;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "alumni_groups")
public class AlumniGroup extends PanacheEntityBase {

    @OneToMany(mappedBy = "group")
    public List<AlumniGroupsMembership> memberships;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groups_id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;
    @Column(name = "group_number")
    private int groupNumber;
    @NotNull
    @NotBlank
    @Column(name = "graduation_year")
    private int graduationYear;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "speciality_id")
    private Speciality speciality;

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

    public List<AlumniGroupsMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<AlumniGroupsMembership> memberships) {
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
