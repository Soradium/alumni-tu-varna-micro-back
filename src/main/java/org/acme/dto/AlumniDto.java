package org.acme.dto;

public class AlumniDto {
    private Long id;
    private Long apiBaseId;      // reference to ApiBase
    private Long degreeId;

    public AlumniDto() {}

    public AlumniDto(Long id, Long apiBaseId, Long degreeId) {
        this.id = id;
        this.apiBaseId = apiBaseId;
        this.degreeId = degreeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApiBaseId() {
        return apiBaseId;
    }

    public void setApiBaseId(Long apiBaseId) {
        this.apiBaseId = apiBaseId;
    }

    public Long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Long degreeId) {
        this.degreeId = degreeId;
    }
}
