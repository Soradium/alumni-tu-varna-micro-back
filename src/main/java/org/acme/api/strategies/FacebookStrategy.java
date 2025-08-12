package org.acme.api.strategies;

import org.acme.avro.back.CompanyRecordDto;

import java.util.ArrayList;
import java.util.List;

public class FacebookStrategy implements CompanyStrategy {

    @Override
    public List<CompanyRecordDto> getCompanies(int alumniId) {
        return new ArrayList<CompanyRecordDto>();//replace by actual logic
    }


}
