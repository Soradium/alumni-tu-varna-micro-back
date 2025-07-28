package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.entites.Specialities;

import java.util.Optional;

public class SpecialitiesRepository implements PanacheRepository<Specialities> {
    public Specialities findByName(String name) {
        return find("speciality", name).firstResult();
    }

    public Optional<Specialities> findByNameOptional(String name) {
        return find("speciality", name).firstResultOptional();
    }
}
