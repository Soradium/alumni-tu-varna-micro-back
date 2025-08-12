package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entites.CompanyRecord;

import java.util.List;

@ApplicationScoped
public class CompanyRecordsRepository implements PanacheRepository<CompanyRecord> {
    public List<CompanyRecord> findByAlumniId(long alumniId) {
        return list("alumni.id", alumniId);
    }

}