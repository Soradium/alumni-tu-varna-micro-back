package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entites.CompanyRecords;

import java.util.List;

@ApplicationScoped
public class CompanyRecordsRepository implements PanacheRepository<CompanyRecords> {
    public List<CompanyRecords> findByAlumniId(long alumniId){
        return list("alumni.id", alumniId);
    }

}