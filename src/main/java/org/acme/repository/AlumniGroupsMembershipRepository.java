package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AlumniGroupsMembershipRepository
        implements PanacheRepository<AlumniGroupsMembership> {
    public AlumniGroupsMembership findByFN(Integer id) {
        return find("alumni.facultyNumber", id).firstResult();
    }

    public Optional<AlumniGroupsMembership> findByFNOptional(Integer id) {
        return find("alumni.facultyNumber", id).firstResultOptional();
    }

    public Optional<AlumniGroup> findGroupsByFacultyNumber(Integer facultyNumber) {
        return find("alumni.facultyNumber", facultyNumber)
                .firstResultOptional()
                .map(AlumniGroupsMembership::getGroup);
    }
}