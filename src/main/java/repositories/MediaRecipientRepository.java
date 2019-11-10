package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import repositories.models.DropicMediaRecipientQuantity;

import java.util.List;

@Repository
public interface MediaRecipientRepository extends JpaRepository<DropicMediaRecipientQuantity, Integer> {

    @Query(value = "SELECT * FROM DROPIC_MEDIA_RECIPIENTS WHERE RECIPIENT_USER_ID = :userId and MEDIA_LOCATION_ID = :mediaLocationId", nativeQuery = true)
    DropicMediaRecipientQuantity getRecipientAndQuantityAtLocation(@Param("userId") Integer userId, @Param("mediaLocationId") Integer mediaLocationId);

    @Query(value = "SELECT * FROM DROPIC_MEDIA_RECIPIENTS WHERE MEDIA_LOCATION_ID = :mediaLocationId", nativeQuery = true)
    List<DropicMediaRecipientQuantity> getRecipientsAndQuantitiesAtLocation(@Param("mediaLocationId") Integer mediaLocationId);
}
