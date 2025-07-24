package org.acme.dto;

import java.time.LocalDate;

public class CompanyDto {
    private Long id;
    private Long alumniId;
    private LocalDate enrollmentDate;
    private LocalDate dischargeDate;
    private String positionName;
    private String companyName;

    public CompanyDto() {}

    public CompanyDto(Long id, Long alumniId, LocalDate enrollmentDate, LocalDate dischargeDate, String positionName, String companyName) {
        this.id = id;
        this.alumniId = alumniId;
        this.enrollmentDate = enrollmentDate;
        this.dischargeDate = dischargeDate;
        this.positionName = positionName;
        this.companyName = companyName;
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
