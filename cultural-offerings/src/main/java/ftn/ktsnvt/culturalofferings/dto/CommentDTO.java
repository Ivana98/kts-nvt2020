package ftn.ktsnvt.culturalofferings.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CommentDTO {
    private Long id;

    @NotBlank(message = "Comment text cannot be empty")
    private String text;

    private Date date;

    private List<Long> imageIds;

    @NotNull(message = "Cultural offering id must be provided")
    @Positive(message = "Cultural offering id must be a positive number")
    private Long culturalOfferingId;

    @NotNull(message = "User id must be provided")
    @Positive(message = "User id must be a positive number")
    private Long userId;

    public CommentDTO() {}

    public CommentDTO(Long id, @NotBlank(message = "Comment text cannot be empty") String text, Date date, List<Long> images, @NotNull(message = "Cultural offering id must be provided") @Positive(message = "Cultural offering id must be a positive number") Long culturalOffering, @NotNull(message = "User id must be provided") @Positive(message = "User id must be a positive number") Long user) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.imageIds = images;
        this.culturalOfferingId = culturalOffering;
        this.userId = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Long> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<Long> imageIds) {
        this.imageIds = imageIds;
    }

    public Long getCulturalOfferingId() {
        return culturalOfferingId;
    }

    public void setCulturalOfferingId(Long culturalOfferingId) {
        this.culturalOfferingId = culturalOfferingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
