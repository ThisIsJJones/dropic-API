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
@Table(name = "DropicMedia")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DropicMedia {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String mediaPath;
    private Integer userDropperID;
    private String name;
    private String message;
    private Date droppedDate;
    private Date pickupDateDeadline;
}
