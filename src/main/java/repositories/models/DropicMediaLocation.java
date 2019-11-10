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
@Table(name = "DropicMediaLocation")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DropicMediaLocation {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private Integer mediaId;
    private Double latitude;
    private Double longitude;
}
