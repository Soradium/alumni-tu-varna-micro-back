package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entites.AlumniDetails;

@ApplicationScoped
public class AlumniDetailsRepository implements PanacheRepository<AlumniDetails> {
}
