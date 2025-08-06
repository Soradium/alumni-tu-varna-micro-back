package org.acme.api.strategies;

import java.util.ArrayList;
import java.util.List;

import org.acme.avro.back.CompanyRecordDto;
public class LinkedInStrategy implements CompanyStrategy{
    
    @Override
    public List<CompanyRecordDto> getCompanies(int alumniId) {
        return new ArrayList<CompanyRecordDto>(); //replace by actual logic
    }

}
