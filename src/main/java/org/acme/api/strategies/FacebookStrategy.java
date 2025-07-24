package org.acme.api.strategies;

import java.util.ArrayList;
import java.util.List;

import org.acme.dto.CompanyDto;

public class FacebookStrategy implements CompanyStrategy{

    @Override
    public List<CompanyDto> getCompanies(long alumniId) {
        return new ArrayList<CompanyDto>();
    }

    
}
