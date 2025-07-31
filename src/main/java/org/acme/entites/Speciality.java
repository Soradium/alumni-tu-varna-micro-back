package org.acme.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "specialities")
public class Speciality extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "speciality_id")
    private Integer id;

    @NotNull
    @NotBlank
    @Size(max = 100)
    @Column(name = "speciality_name")
    private String specialityName;

    public Speciality() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotNull @NotBlank @Size(max = 100) String getSpeciality() {
        return specialityName;
    }

    public void setSpecialityName(@NotNull @NotBlank @Size(max = 100) String specialityName) {
        this.specialityName = specialityName;
    }

    public String getSpecialityName() {
        return specialityName;
    }
    
}
