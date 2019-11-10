package presentation.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import services.dtos.MediaDropDetails;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class MediaDropRequest {
    private Integer userID;
    private String nameOfMedia;
    private String message;
    private byte[] mediaBytes;
    private Date droppedDate;
    private Date pickupDateDeadline;
    private Boolean isRandomDrop;
    private List<MediaDropDetails> mediaDropDetails;
}
