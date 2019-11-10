package presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import presentation.requests.MediaDropRequest;
import presentation.responses.NearbyMediaResponse;
import repositories.models.DropicMedia;
import services.MediaService;
import services.dtos.Location;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

@RestController
@RequestMapping(value = "/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @RequestMapping(value = "/getNearby/{userID}/{latitude}/{longitude}")
    public List<NearbyMediaResponse> getMediaNearby(@PathVariable Integer userID, @PathVariable Double latitude, @PathVariable Double longitude) {
        System.out.println("USER LAT: " + latitude);
        System.out.println("USER LONG: " + longitude);
        
        

        // returns list of medias that are nearby the given coordinates
        return mediaService.getMediaNearby(userID, latitude, longitude);
    }

    @RequestMapping(value = "/grab/{mediaID}/{mediaLocationID}/{userID}/{latitude}/{longitude}")
    public DropicMedia grabMedia(@PathVariable Integer mediaID, @PathVariable Integer mediaLocationID, @PathVariable Integer userID, @PathVariable Double latitude, @PathVariable Double longitude) {
        System.out.println(mediaID);
        System.out.println(mediaLocationID);
        System.out.println(userID);
        Location userLocation =  Location.builder()
                .latitude(latitude)
                .longitude(longitude).build();
        return mediaService.grabMedia(mediaID, mediaLocationID, userID, userLocation);

    }
    
    @RequestMapping(value = "/grabAllUsersMedia/{userID}")
    public List<DropicMedia> getUsersMedia(@PathVariable Integer userID) {
        return mediaService.getAllUsersMedia(userID);

    }

    @RequestMapping(value = "/drop")
    public Boolean dropMedia(@RequestBody MediaDropRequest dropMedia) {
        System.out.println("DROPPING: " + dropMedia.getNameOfMedia());
        System.out.println("LAT: " + dropMedia.getMediaDropDetails().get(0).getDropLocation().getLatitude());
        System.out.println("LONG: " + dropMedia.getMediaDropDetails().get(0).getDropLocation().getLongitude());
        
//        try {
	        
	        try {
	        	ByteArrayInputStream bis = new ByteArrayInputStream(dropMedia.getMediaBytes());
		        BufferedImage bImage2 = ImageIO.read(bis);
				ImageIO.write(bImage2, "jpg", new File("/Users/jordanjones/Desktop/output.jpg") );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
//        } catch (ImageIO e) {
//        	System.out.println("ERROR IO");
//        }
        mediaService.dropMedia(dropMedia);
        return true;
    }
}
