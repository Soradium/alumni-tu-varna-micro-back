package org.acme.repository;

import java.util.Optional;

import org.acme.entites.Degree;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DegreeRepository implements PanacheRepository<Degree> {
    public Degree findByName(String degree) {
        return find("degreeName", degree).firstResult();
    }

    public Optional<Degree> findByNameOptional(String degree) {
        return find("degreeName", degree).firstResultOptional();
    }
}
