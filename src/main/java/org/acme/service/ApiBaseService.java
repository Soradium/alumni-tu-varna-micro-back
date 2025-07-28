package org.acme.service;

import org.acme.dto.ApiBaseDto;
import org.acme.entites.ApiBase;

public interface ApiBaseService {
    ApiBaseDto getApiBase(long id);
    ApiBaseDto getApiBase(ApiBaseDto dto);

    ApiBaseDto createApiBase(ApiBaseDto dto);
    ApiBaseDto updateApiBase(long id, ApiBaseDto dto);
    ApiBaseDto deleteApiBase(long id);

    boolean isApiBaseExist(long id);
    boolean isApiBaseExist(ApiBaseDto dto);
}
