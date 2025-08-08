package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entites.Alumni;
import org.acme.entites.AlumniDetails;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AlumniDetailsRepository implements PanacheRepository<AlumniDetails> {
    public List<AlumniDetails> findAllAlumniDetailsForAllAlumni(List<Alumni> alumni) throws Exception {
        return alumni.stream()
                .map(a -> find("facultyNumber", a.getFacultyNumber())
                        .firstResultOptional()
                        .orElseThrow(() -> new RuntimeException(
                                "No such Alumni faculty number exists in AlumniDetails database")))
                .collect(Collectors.toList());
    }

}
