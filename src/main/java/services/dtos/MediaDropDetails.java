package services.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MediaDropDetails {
    private List<RecipientAndPickupQuantity> recipientsAndPickupQuantities;
    private Location dropLocation;
}
