package org.acme.entites;

import java.sql.Timestamp;
import java.time.LocalDate;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "company_records")
public class CompanyRecord extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_record_id")
    private Integer id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "faculty_number")
    private Alumni alumni;

    @NotBlank
    @NotNull
    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Column(name = "discharge_date")
    private LocalDate dischargeDate;

    @NotNull
    @NotBlank
    @Size(max = 255)
    @Column(name = "position_name")
    private String position;

    @NotNull
    @NotBlank
    @Size(max = 255)
    @Column(name = "company_name")
    private String company;

    @Column(name = "created_at")
    private Timestamp createdAt;

    public CompanyRecord() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
}
