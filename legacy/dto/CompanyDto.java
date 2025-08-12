package org.acme.dto;

import java.time.LocalDate;

public class CompanyDto {
    private Integer id;
    private Integer facultyNumber;
    private LocalDate enrollmentDate;
    private LocalDate dischargeDate;
    private String positionName;
    private String companyName;

    public CompanyDto() {}

    public CompanyDto(Integer id, Integer facultyNumber, LocalDate enrollmentDate, LocalDate dischargeDate, String positionName, String companyName) {
        this.id = id;
        this.facultyNumber = facultyNumber;
        this.enrollmentDate = enrollmentDate;
        this.dischargeDate = dischargeDate;
        this.positionName = positionName;
        this.companyName = companyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlumniId() {
        return facultyNumber;
    }

    public void setAlumniId(Integer alumniId) {
        this.facultyNumber = alumniId;
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

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
