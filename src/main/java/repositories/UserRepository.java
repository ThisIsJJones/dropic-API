package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import repositories.models.DropicUser;

@Repository
public interface UserRepository extends JpaRepository<DropicUser, Integer>
{

}
