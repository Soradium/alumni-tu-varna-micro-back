package org.acme.util.mappers;

import org.acme.dto.ApiBaseDto;
import org.acme.entites.ApiBase;

public class ApiBaseMapper {
    public ApiBaseDto toDto(ApiBase apiBase) {
        ApiBaseDto dto = new ApiBaseDto();
        dto.setId(apiBase.getId());
        dto.setFacebookUrl(apiBase.getFacebookUrl());
        dto.setLinkedinUrl(apiBase.getLinkedinUrl());
        return dto;
    }

    public ApiBase toEntity(ApiBaseDto apiBaseDto) {
        ApiBase entity = new ApiBase();
        entity.setFacebookUrl(apiBaseDto.getFacebookUrl());
        entity.setLinkedinUrl(apiBaseDto.getLinkedinUrl());
        return entity;
    }

}
