package charactercreator.repository;

import charactercreator.domain.ExperiencePoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ExperiencePoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExperiencePointRepository extends JpaRepository<ExperiencePoint, Long> {

}
