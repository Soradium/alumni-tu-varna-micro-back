package org.acme.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.kafka.common.protocol.types.Field;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;

@Entity
@Table(name = "alumni_details")
public class AlumniDetails extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alumni_id")
    private Long id;

    @Column(name = "full_name")
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String fullName;

    @Column(name = "faculty_number")
    @NotNull
    @Size(max = 100)
    private String facultyNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "faculty_id")
    private Faculties faculties;

    public AlumniDetails() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull @NotBlank @Size(max = 255) String getFullName() {
        return fullName;
    }

    public void setFullName(@NotNull @NotBlank @Size(max = 255) String fullName) {
        this.fullName = fullName;
    }

    public @NotNull @Size(max = 100) String getFacultyNumber() {
        return facultyNumber;
    }

    public void setFacultyNumber(@NotNull @Size(max = 100) String facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Faculties getFaculties() {
        return faculties;
    }

    public void setFaculties(Faculties faculties) {
        this.faculties = faculties;
    }
}
