package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entites.Alumni;
import org.acme.entites.Degree;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AlumniRepository implements PanacheRepository<Alumni> {
    public Optional<Alumni> findByDegreeOptional(Degree degree) {
        return find("degree", degree).firstResultOptional();
    }

    public List<Alumni> findAllAlumniByDegree(String degreeName) {
        return Alumni.find("degree.degreeName", degreeName).list();
    }

}
