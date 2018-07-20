package charactercreator.repository;

import charactercreator.domain.CharacterRace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the CharacterRace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacterRaceRepository extends JpaRepository<CharacterRace, Long> {

    @Query(value = "select distinct character_race from CharacterRace character_race left join fetch character_race.skills",
        countQuery = "select count(distinct character_race) from CharacterRace character_race")
    Page<CharacterRace> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct character_race from CharacterRace character_race left join fetch character_race.skills")
    List<CharacterRace> findAllWithEagerRelationships();

    @Query("select character_race from CharacterRace character_race left join fetch character_race.skills where character_race.id =:id")
    Optional<CharacterRace> findOneWithEagerRelationships(@Param("id") Long id);

}
