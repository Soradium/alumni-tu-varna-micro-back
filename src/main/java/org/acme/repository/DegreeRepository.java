package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entites.Degree;

import java.util.Optional;

@ApplicationScoped
public class DegreeRepository implements PanacheRepository<Degree> {
    public Degree findByName(String degree) {
        return find("degree", degree).firstResult();
    }

    public Optional<Degree> findByNameOptional(String degree) {
        return find("degree", degree).firstResultOptional();
    }
}
