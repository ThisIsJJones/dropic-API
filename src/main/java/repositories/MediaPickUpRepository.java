package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import repositories.models.DropicUserPickedUpMedia;

@Repository
public interface MediaPickUpRepository extends JpaRepository<DropicUserPickedUpMedia, Integer> {

    @Query(value = "SELECT * FROM DROPIC_MEDIA_PICK_UP WHERE USERID = :userId and MEDIAID = :mediaId", nativeQuery = true)
    DropicUserPickedUpMedia getUserWithMedia(@Param("userId") Integer userId, @Param("mediaId") Integer mediaId);

    @Query(value = "SELECT * FROM DROPIC_MEDIA_PICK_UP WHERE USERID = :userId ", nativeQuery = true)
    List<DropicUserPickedUpMedia> getAllUsersPickedUpMedia(@Param("userId") Integer userId);
}
