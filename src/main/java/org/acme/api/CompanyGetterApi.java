package org.acme.api;
import org.acme.exceptions.IncorrectAlumnusNumberException;

import java.util.List;

import org.acme.api.strategies.CompanyStrategy;
import org.acme.dto.CompanyDto;

public class CompanyGetterApi {
    private CompanyStrategy strategy;

    public CompanyGetterApi(CompanyStrategy strategy) {
        this.strategy = strategy;
    }
    
    public List<CompanyDto> getCompaniesPerAlumni(int alumniId) throws Exception {
        // possible bad outcomes: no strategy set, null list
        // positive: list that has companies, empty list
        if(this.strategy == null) {
            throw new NullPointerException("No strategy set.");
        }
        if(alumniId <= 0) {
            throw new IncorrectAlumnusNumberException("s");
        }
        List<CompanyDto> companies = strategy.getCompanies(alumniId); 
        if(companies == null) {
            throw new NullPointerException("No companies list loaded.");
        }
        return companies;
    }


    public void setCompanyStrategy(CompanyStrategy strategy) {
        this.strategy = strategy;
    }

}
