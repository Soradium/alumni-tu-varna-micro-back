package org.acme.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Faculties extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faculty_id")
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "faculty_name")
    @Size(max = 100)
    private String facultyName;

    public Faculties() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull @NotBlank @Size(max = 100) String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(@NotNull @NotBlank @Size(max = 100) String facultyName) {
        this.facultyName = facultyName;
    }
}
