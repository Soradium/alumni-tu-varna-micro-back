package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entites.Speciality;

import java.util.Optional;

@ApplicationScoped
public class SpecialityRepository implements PanacheRepository<Speciality> {
    public Speciality findByName(String name) {
        return find("speciality", name).firstResult();
    }

    public Optional<Speciality> findByNameOptional(String name) {
        return find("speciality", name).firstResultOptional();
    }
}
