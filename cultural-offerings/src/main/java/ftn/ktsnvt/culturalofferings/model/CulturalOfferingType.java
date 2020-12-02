package ftn.ktsnvt.culturalofferings.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CulturalOfferingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String typeName;

    @OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn
    private ImageModel imageModel;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "culturalOfferingType")
    private Set<CulturalOfferingSubType> culturalOfferingSubTypes;

    public CulturalOfferingType(){}

    public CulturalOfferingType(String typeName, ImageModel imageModel) {
        this.typeName = typeName;
        this.imageModel = imageModel;
        this.culturalOfferingSubTypes = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public ImageModel getImageModel() {
        return imageModel;
    }

    public void setImageModel(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public Set<CulturalOfferingSubType> getCulturalOfferingSubTypes() {
        return culturalOfferingSubTypes;
    }

    public void setCulturalOfferingSubTypes(Set<CulturalOfferingSubType> culturalOfferingSubTypes) {
        this.culturalOfferingSubTypes = culturalOfferingSubTypes;
    }
}
