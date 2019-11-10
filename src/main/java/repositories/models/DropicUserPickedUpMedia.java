package repositories.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "DropicMediaPickUp")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DropicUserPickedUpMedia {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private Integer userID;
    private Integer mediaID;
    private Date pickedUpDate;
}
