package org.acme.api.strategies;

import java.util.List;
import org.acme.dto.CompanyDto;

public interface CompanyStrategy {
    public List<CompanyDto> getCompanies(int alumniId);
}
