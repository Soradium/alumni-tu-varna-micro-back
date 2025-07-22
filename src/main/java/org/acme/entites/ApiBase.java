package org.acme.entites;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "api_base")
public class ApiBase extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facebook_url")
    @Size(min = 10, max = 300)
    private String facebookUrl;

    @Column(name = "linkedin_url")
    @Size(min = 10, max = 300)
    private String linkedinUrl;

    public ApiBase() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @Size(min = 10, max = 300) String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(@Size(min = 10, max = 300) String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public @Size(min = 10, max = 300) String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(@Size(min = 10, max = 300) String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }
}
