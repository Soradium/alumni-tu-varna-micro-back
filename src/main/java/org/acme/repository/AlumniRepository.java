package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entites.Alumni;

import java.util.Optional;

@ApplicationScoped
public class AlumniRepository implements PanacheRepository<Alumni> {
    public Optional<Alumni> findByApiBaseId(Long apiBaseId) {
        return find("apiBase.id", apiBaseId).firstResultOptional();
    }
}
