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

@Entity
@Table(name = "DropicUser")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DropicUser {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String username;
    private String passwordHash;
    private Integer salt;
}
