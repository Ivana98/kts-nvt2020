package ftn.ktsnvt.culturalofferings.model;

import javax.persistence.*;

@Entity
public class CulturalOfferingSubType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String subTypeName;

    @ManyToOne(fetch = FetchType.EAGER)
    private CulturalOfferingType culturalOfferingType;

    public CulturalOfferingSubType(){}

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

    public CulturalOfferingType getCulturalOfferingType() {
        return culturalOfferingType;
    }

    public void setCulturalOfferingType(CulturalOfferingType culturalOfferingType) {
        this.culturalOfferingType = culturalOfferingType;
    }
}