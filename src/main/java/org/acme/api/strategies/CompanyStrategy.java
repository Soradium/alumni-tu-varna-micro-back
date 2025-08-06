package org.acme.api.strategies;

import java.util.List;

import org.acme.avro.back.CompanyRecordDto;
public interface CompanyStrategy {
    public List<CompanyRecordDto> getCompanies(int alumniId);
}
