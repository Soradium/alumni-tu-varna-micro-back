package org.acme.dto;

import org.acme.entites.Faculty;
import java.time.LocalDate;

public class AlumniDetailsDto {
    private Integer facultyNumber;
    private String fullName;
    private LocalDate birthDate;
    private Integer facultyId;

    public AlumniDetailsDto() {}

    public Integer getFacultyNumber() {
        return facultyNumber;
    }

    public void setFacultyNumber(Integer facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getFaculty() {
        return facultyId;
    }

    public void setFaculty(Integer faculty) {
        this.facultyId = faculty;
    }
}
