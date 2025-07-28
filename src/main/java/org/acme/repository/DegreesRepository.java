package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.entites.Degrees;

import java.util.Optional;

public class DegreesRepository implements PanacheRepository<Degrees> {
    public Degrees findByName(String degree){
        return find("degree", degree).firstResult();
    }
    public Optional<Degrees> findByNameOptional(String degree){
        return find("degree", degree).firstResultOptional();
    }
}
