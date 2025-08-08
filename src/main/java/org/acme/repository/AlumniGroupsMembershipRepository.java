package org.acme.repository;

import java.util.List;
import java.util.Optional;

import org.acme.entites.AlumniGroup;
import org.acme.entites.AlumniGroupsMembership;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AlumniGroupsMembershipRepository
        implements PanacheRepository<AlumniGroupsMembership> {
    public AlumniGroupsMembership findByFacultyNumber(Integer facultyNumber) {
        return find("alumni.facultyNumber", facultyNumber).firstResult();
    }

    public Optional<AlumniGroupsMembership> findByFacultyNumberOptional(Integer facultyNumber) {
        return find("alumni.facultyNumber", facultyNumber).firstResultOptional();
    }

    public List<AlumniGroupsMembership> findAllByFacultyNumber(Integer facultyNumber) {
        return find("alumni.facultyNumber", facultyNumber).list();
    }
    
    public List<AlumniGroupsMembership> findAllByGroupId(Integer groupId) {
        return find("group.id", groupId).list();
    }
    
    public Optional<AlumniGroupsMembership> findByGroupIdOptional(Integer groupId) {
        return find("group.id", groupId).firstResultOptional();
    }

    public Optional<AlumniGroupsMembership> findByGroupOptional(AlumniGroup group) {
        return find("group", group).firstResultOptional();
    }

    public Optional<AlumniGroup> findGroupsByFacultyNumber(Integer facultyNumber) {
        return find("alumni.facultyNumber", facultyNumber)
                .firstResultOptional()
                .map(AlumniGroupsMembership::getGroup);
    }

    
}