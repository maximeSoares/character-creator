package charactercreator.repository;

import charactercreator.domain.CharacterClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the CharacterClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacterClassRepository extends JpaRepository<CharacterClass, Long> {

    @Query(value = "select distinct character_class from CharacterClass character_class left join fetch character_class.skills",
        countQuery = "select count(distinct character_class) from CharacterClass character_class")
    Page<CharacterClass> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct character_class from CharacterClass character_class left join fetch character_class.skills")
    List<CharacterClass> findAllWithEagerRelationships();

    @Query("select character_class from CharacterClass character_class left join fetch character_class.skills where character_class.id =:id")
    Optional<CharacterClass> findOneWithEagerRelationships(@Param("id") Long id);

}
