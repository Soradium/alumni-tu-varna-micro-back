package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entites.AlumniGroup;

import java.util.Optional;

@ApplicationScoped
public class AlumniGroupRepository implements PanacheRepository<AlumniGroup> {
    public AlumniGroup findByGroupNumber(int id) {
        return find("groupNumber", id).firstResult();
    }

    public Optional<AlumniGroup> findByGroupNumberOptional(int id) {
        return find("groupNumber", id).firstResultOptional();
    }
}
