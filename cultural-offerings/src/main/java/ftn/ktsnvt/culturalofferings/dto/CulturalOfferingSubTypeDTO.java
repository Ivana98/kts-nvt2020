package ftn.ktsnvt.culturalofferings.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CulturalOfferingSubTypeDTO {

    private Long id;

    @NotBlank(message = "Cultural offering subtype name cannot be empty")
    private String subTypeName;

    @NotNull(message = "Cultural offering type id must be provided")
    @Positive(message = "Cultural offering type id must be a positive number")
    private Long typeId;

    public CulturalOfferingSubTypeDTO(Long id, String subTypeName, Long typeId) {
        this.id = id;
        this.subTypeName = subTypeName;
        this.typeId = typeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
}
