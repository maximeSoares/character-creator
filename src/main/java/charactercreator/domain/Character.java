package charactercreator.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Character.
 */
@Entity
@Table(name = "character")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Character implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "character_skill",
               joinColumns = @JoinColumn(name = "characters_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "skills_id", referencedColumnName = "id"))
    private Set<Skill> skills = new HashSet<>();

    @OneToMany(mappedBy = "character")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ExperiencePoint> experiencePoints = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Character name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public Character user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public Character skills(Set<Skill> skills) {
        this.skills = skills;
        return this;
    }

    public Character addSkill(Skill skill) {
        this.skills.add(skill);
        skill.getCharacters().add(this);
        return this;
    }

    public Character removeSkill(Skill skill) {
        this.skills.remove(skill);
        skill.getCharacters().remove(this);
        return this;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public Set<ExperiencePoint> getExperiencePoints() {
        return experiencePoints;
    }

    public Character experiencePoints(Set<ExperiencePoint> experiencePoints) {
        this.experiencePoints = experiencePoints;
        return this;
    }

    public Character addExperiencePoint(ExperiencePoint experiencePoint) {
        this.experiencePoints.add(experiencePoint);
        experiencePoint.setCharacter(this);
        return this;
    }

    public Character removeExperiencePoint(ExperiencePoint experiencePoint) {
        this.experiencePoints.remove(experiencePoint);
        experiencePoint.setCharacter(null);
        return this;
    }

    public void setExperiencePoints(Set<ExperiencePoint> experiencePoints) {
        this.experiencePoints = experiencePoints;
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
        Character character = (Character) o;
        if (character.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), character.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Character{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
