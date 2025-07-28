package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entites.ApiBase;

import java.util.Optional;

@ApplicationScoped
public class ApiBaseRepository implements PanacheRepository<ApiBase> {
    public ApiBase findByFacebookOrLinkedIn(String facebookUrl, String linkedinUrl) {
            return find("facebookUrl = ?1 OR linkedinUrl = ?2",
                    facebookUrl, linkedinUrl).firstResult();
        }
    }
