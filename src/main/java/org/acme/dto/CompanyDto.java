package org.acme.dto;

import java.time.LocalDate;

public class CompanyDto {
    private Long id;
    private Long alumniId;
    private LocalDate enrollmentDate;
    private LocalDate dischargeDate;
    private String position;
    private String company;

    public CompanyDto() {}

    public CompanyDto(Long id, Long alumniId, LocalDate enrollmentDate, LocalDate dischargeDate, String position, String company) {
        this.id = id;
        this.alumniId = alumniId;
        this.enrollmentDate = enrollmentDate;
        this.dischargeDate = dischargeDate;
        this.position = position;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlumniId() {
        return alumniId;
    }

    public void setAlumniId(Long alumniId) {
        this.alumniId = alumniId;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public LocalDate getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDate dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
