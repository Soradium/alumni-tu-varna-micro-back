package org.acme.dto;

import java.time.LocalDate;

public class AlumniDetailsDto {
    private Long alumniId;       // matches AlumniDetails.id
    private String fullName;
    private String facultyNumber;
    private LocalDate birthDate;
    private Long facultyId;

    public AlumniDetailsDto() {}

    public AlumniDetailsDto(Long alumniId, String fullName, String facultyNumber, LocalDate birthDate, Long facultyId) {
        this.alumniId = alumniId;
        this.fullName = fullName;
        this.facultyNumber = facultyNumber;
        this.birthDate = birthDate;
        this.facultyId = facultyId;
    }

    public Long getAlumniId() {
        return alumniId;
    }

    public void setAlumniId(Long alumniId) {
        this.alumniId = alumniId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFacultyNumber() {
        return facultyNumber;
    }

    public void setFacultyNumber(String facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }
}
