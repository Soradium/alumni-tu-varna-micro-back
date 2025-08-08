package org.acme.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "degrees")
public class Degree extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "degree_id")
    private Integer id;

    @NotNull
    @NotBlank
    @Size(max = 100)
    @Column(name = "degree_name")
    private String degreeName;

    public Degree() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotNull @NotBlank @Size(max = 100) String getDegreeName() {
        return degreeName;
    }

    public void setDegree(@NotNull @NotBlank @Size(max = 100) String degreeName) {
        this.degreeName = degreeName;
    }


}
