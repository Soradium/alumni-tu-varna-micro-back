package org.acme.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "company_records")
public class CompanyRecords extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "alumni_id")
    private Alumni alumni;

    @NotBlank
    @NotNull
    private LocalDate enrollmentDate;

    private LocalDate dischargeDate;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String position;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String company;

    public CompanyRecords() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Alumni getAlumni() {
        return alumni;
    }

    public void setAlumni(Alumni alumni) {
        this.alumni = alumni;
    }

    public @NotBlank @NotNull LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(@NotBlank @NotNull LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public LocalDate getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDate dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public @NotNull @NotBlank @Size(max = 255) String getPosition() {
        return position;
    }

    public void setPosition(@NotNull @NotBlank @Size(max = 255) String position) {
        this.position = position;
    }

    public @NotNull @NotBlank @Size(max = 255) String getCompany() {
        return company;
    }

    public void setCompany(@NotNull @NotBlank @Size(max = 255) String company) {
        this.company = company;
    }
}
