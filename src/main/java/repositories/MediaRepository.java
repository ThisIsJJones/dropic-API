package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import repositories.models.DropicMedia;

@Repository
public interface MediaRepository extends JpaRepository<DropicMedia, Integer> {
}
