package charactercreator.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A ExperiencePoint.
 */
@Entity
@Table(name = "experience_point")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExperiencePoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "acquisition_date", nullable = false)
    private Instant acquisitionDate;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "starting_experience_point", nullable = false)
    private Boolean startingExperiencePoint;

    @ManyToOne
    @JsonIgnoreProperties("experiencePoints")
    private Character character;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAcquisitionDate() {
        return acquisitionDate;
    }

    public ExperiencePoint acquisitionDate(Instant acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
        return this;
    }

    public void setAcquisitionDate(Instant acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getDescription() {
        return description;
    }

    public ExperiencePoint description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isStartingExperiencePoint() {
        return startingExperiencePoint;
    }

    public ExperiencePoint startingExperiencePoint(Boolean startingExperiencePoint) {
        this.startingExperiencePoint = startingExperiencePoint;
        return this;
    }

    public void setStartingExperiencePoint(Boolean startingExperiencePoint) {
        this.startingExperiencePoint = startingExperiencePoint;
    }

    public Character getCharacter() {
        return character;
    }

    public ExperiencePoint character(Character character) {
        this.character = character;
        return this;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExperiencePoint experiencePoint = (ExperiencePoint) o;
        if (experiencePoint.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), experiencePoint.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExperiencePoint{" +
            "id=" + getId() +
            ", acquisitionDate='" + getAcquisitionDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", startingExperiencePoint='" + isStartingExperiencePoint() + "'" +
            "}";
    }
}
