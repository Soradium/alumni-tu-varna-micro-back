package org.acme.api.strategies;

import org.acme.avro.back.CompanyRecordDto;

import java.util.List;

public interface CompanyStrategy {
    List<CompanyRecordDto> getCompanies(int alumniId);
}
