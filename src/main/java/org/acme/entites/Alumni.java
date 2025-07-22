package org.acme.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name = "alumni")
public class Alumni extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "api_base_id")
    private ApiBase apiBaseId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "degree_id")
    private Degrees degrees;

    @OneToMany(mappedBy = "alumni")
    public ArrayList<AlumniGroupsMembership> memberships;

    public Alumni() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApiBase getApiBaseId() {
        return apiBaseId;
    }

    public void setApiBaseId(ApiBase apiBaseId) {
        this.apiBaseId = apiBaseId;
    }

    public Degrees getDegrees() {
        return degrees;
    }

    public void setDegrees(Degrees degrees) {
        this.degrees = degrees;
    }

    public ArrayList<AlumniGroupsMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(ArrayList<AlumniGroupsMembership> memberships) {
        this.memberships = memberships;
    }
}
