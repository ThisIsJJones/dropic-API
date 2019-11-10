package repositories.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DropicMediaRecipients")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DropicMediaRecipientQuantity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private Integer mediaLocationId;
    private Integer recipientUserId;
    private Integer quantityToPickup;
}
