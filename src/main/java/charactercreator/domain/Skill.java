package charactercreator.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Skill.
 */
@Entity
@Table(name = "skill")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Skill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description_short")
    private String descriptionShort;

    @Column(name = "description_long")
    private String descriptionLong;

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Character> characters = new HashSet<>();

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CharacterClass> characterClasses = new HashSet<>();

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CharacterRace> characterRaces = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Skill title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public Skill descriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
        return this;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionLong() {
        return descriptionLong;
    }

    public Skill descriptionLong(String descriptionLong) {
        this.descriptionLong = descriptionLong;
        return this;
    }

    public void setDescriptionLong(String descriptionLong) {
        this.descriptionLong = descriptionLong;
    }

    public Set<Character> getCharacters() {
        return characters;
    }

    public Skill characters(Set<Character> characters) {
        this.characters = characters;
        return this;
    }

    public Skill addCharacter(Character character) {
        this.characters.add(character);
        character.getSkills().add(this);
        return this;
    }

    public Skill removeCharacter(Character character) {
        this.characters.remove(character);
        character.getSkills().remove(this);
        return this;
    }

    public void setCharacters(Set<Character> characters) {
        this.characters = characters;
    }

    public Set<CharacterClass> getCharacterClasses() {
        return characterClasses;
    }

    public Skill characterClasses(Set<CharacterClass> characterClasses) {
        this.characterClasses = characterClasses;
        return this;
    }

    public Skill addCharacterClass(CharacterClass characterClass) {
        this.characterClasses.add(characterClass);
        characterClass.getSkills().add(this);
        return this;
    }

    public Skill removeCharacterClass(CharacterClass characterClass) {
        this.characterClasses.remove(characterClass);
        characterClass.getSkills().remove(this);
        return this;
    }

    public void setCharacterClasses(Set<CharacterClass> characterClasses) {
        this.characterClasses = characterClasses;
    }

    public Set<CharacterRace> getCharacterRaces() {
        return characterRaces;
    }

    public Skill characterRaces(Set<CharacterRace> characterRaces) {
        this.characterRaces = characterRaces;
        return this;
    }

    public Skill addCharacterRace(CharacterRace characterRace) {
        this.characterRaces.add(characterRace);
        characterRace.getSkills().add(this);
        return this;
    }

    public Skill removeCharacterRace(CharacterRace characterRace) {
        this.characterRaces.remove(characterRace);
        characterRace.getSkills().remove(this);
        return this;
    }

    public void setCharacterRaces(Set<CharacterRace> characterRaces) {
        this.characterRaces = characterRaces;
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
        Skill skill = (Skill) o;
        if (skill.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), skill.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Skill{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", descriptionShort='" + getDescriptionShort() + "'" +
            ", descriptionLong='" + getDescriptionLong() + "'" +
            "}";
    }
}
