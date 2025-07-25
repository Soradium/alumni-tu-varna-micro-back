package org.acme.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Degree extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "degree_id")
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String degree;

    public Degree() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull @NotBlank @Size(max = 100) String getDegree() {
        return degree;
    }

    public void setDegree(@NotNull @NotBlank @Size(max = 100) String degree) {
        this.degree = degree;
    }
}
