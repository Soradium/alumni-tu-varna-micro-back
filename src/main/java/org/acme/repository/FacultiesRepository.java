package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.entites.Faculties;

import java.util.Optional;

public class FacultiesRepository implements PanacheRepository<Faculties> {
    public Faculties findByName(String name) {
        return find("facultyName", name).firstResult();
    }

    public Optional<Faculties> findByNameOptional(String name) {
        return find("facultyName", name).firstResultOptional();
    }
}
