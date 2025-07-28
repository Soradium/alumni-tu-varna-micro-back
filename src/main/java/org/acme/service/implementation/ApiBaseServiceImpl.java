package org.acme.service.implementation;

import org.acme.dto.ApiBaseDto;
import org.acme.entites.ApiBase;
import org.acme.exceptions.DuplicateResourceException;
import org.acme.exceptions.ResourceNotFoundException;
import org.acme.repository.ApiBaseRepository;
import org.acme.service.ApiBaseService;
import org.acme.util.mappers.ApiBaseMapper;

public class ApiBaseServiceImpl implements ApiBaseService {

    private final ApiBaseRepository apiBaseRepository;
    private final ApiBaseMapper apiBaseMapper;

    public ApiBaseServiceImpl(ApiBaseRepository apiBaseRepository, ApiBaseMapper apiBaseMapper) {
        this.apiBaseRepository = apiBaseRepository;
        this.apiBaseMapper = apiBaseMapper;
    }


    @Override
    public ApiBaseDto getApiBase(long id) {
        //TODO: check id
        ApiBase apiBase = apiBaseRepository.findByIdOptional(id).orElseThrow(
                () -> new ResourceNotFoundException("ApiBase with ID " + id + " not found")
        );
        return apiBaseMapper.toDto(apiBase);
    }

    @Override
    public ApiBaseDto createApiBase(ApiBaseDto dto) {
        ApiBase apiBase = apiBaseMapper.toEntity(dto);
        apiBaseRepository.persist(apiBase);
        return apiBaseMapper.toDto(apiBase);
    }

    @Override
    public ApiBaseDto updateApiBase(long id, ApiBaseDto dto) {
        ApiBase apiBase = apiBaseRepository.findByIdOptional(id).orElseThrow(
                () -> new ResourceNotFoundException("ApiBase with ID " + dto.getId() + " not found")
        );
        if(dto.getFacebookUrl() != null) {
            apiBase.setFacebookUrl(dto.getFacebookUrl());
        }
        if(dto.getLinkedinUrl() != null) {
            apiBase.setLinkedinUrl(dto.getLinkedinUrl());
        }
        apiBaseRepository.persist(apiBase);
        return apiBaseMapper.toDto(apiBase);
    }

    @Override
    public ApiBaseDto deleteApiBase(long id) {
        //TODO: check id
        ApiBase apiBase = apiBaseRepository.findByIdOptional(id).orElseThrow(
                () -> new ResourceNotFoundException("ApiBase with ID " + id + " not found")
        );
        apiBaseRepository.delete(apiBase);
        return apiBaseMapper.toDto(apiBase);
    }

    @Override
    public boolean isApiBaseExist(long id) {
        //TODO: check id
        ApiBase apiBase = apiBaseRepository.findById(id);
        if(apiBase == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isApiBaseExist(ApiBaseDto dto) {
        ApiBase dbRecord = apiBaseRepository.findByFacebookOrLinkedIn(
                dto.getFacebookUrl(), dto.getLinkedinUrl()
        );
        if(dbRecord == null) {
            return false;
        }
        return true;
    }

    @Override
    public ApiBaseDto getApiBase(ApiBaseDto dto) {
        ApiBase dbRecord = apiBaseRepository.findByFacebookOrLinkedIn(
                dto.getFacebookUrl(), dto.getLinkedinUrl()
        );
        return apiBaseMapper.toDto(dbRecord);
    }
}
