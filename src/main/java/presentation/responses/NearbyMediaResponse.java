package presentation.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class NearbyMediaResponse {

    private Integer mediaId;
    private Integer mediaLocationId;
    private Boolean isNearby;
    private Double longitude;
    private Double latitude;
    private String name;
}
