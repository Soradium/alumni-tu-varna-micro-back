package org.acme.dto;

import org.acme.entites.ApiBase;

public class AlumniDto {
    private Long id;
    private ApiBaseDto apiBase;
    private Long degreeId;
    //ad list of memberships

    public AlumniDto() {}

    public AlumniDto(Long id, ApiBaseDto apiBase, Long degreeId) {
        this.id = id;
        this.apiBase = apiBase;
        this.degreeId = degreeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApiBaseDto getApiBase() {
        return apiBase;
    }

    public void setApiBase(ApiBaseDto apiBase) {
        this.apiBase = apiBase;
    }

    public Long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Long degreeId) {
        this.degreeId = degreeId;
    }
}
