package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import repositories.models.DropicMediaLocation;

@Repository
public interface MediaLocationRepository extends JpaRepository<DropicMediaLocation, Integer> {
}
