package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entites.Faculty;

import java.util.Optional;

@ApplicationScoped
public class FacultyRepository implements PanacheRepository<Faculty> {
    public Faculty findByName(String name) {
        return find("facultyName", name).firstResult();
    }

    public Optional<Faculty> findByNameOptional(String name) {
        return find("facultyName", name).firstResultOptional();
    }
}
