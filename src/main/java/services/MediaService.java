package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import presentation.requests.MediaDropRequest;
import presentation.responses.NearbyMediaResponse;
import repositories.MediaLocationRepository;
import repositories.MediaPickUpRepository;
import repositories.MediaRecipientRepository;
import repositories.MediaRepository;
import repositories.models.DropicMedia;
import repositories.models.DropicMediaLocation;
import repositories.models.DropicMediaRecipientQuantity;
import repositories.models.DropicUserPickedUpMedia;
import services.dtos.Location;
import services.dtos.MediaDropDetails;
import services.dtos.RecipientAndPickupQuantity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private MediaRecipientRepository mediaRecipientRepository;

    @Autowired
    private MediaLocationRepository mediaLocationRepository;

    @Autowired
    private MediaPickUpRepository mediaPickUpRepository;

    public List<NearbyMediaResponse> getMediaNearby(Integer userID, Double latitude, Double longitude) {
       
//    	MediaDropRequest mediaDropRequest = new MediaDropRequest();
//    	mediaDropRequest.setNameOfMedia("Zneeches");
//    	
//    	List<MediaDropDetails> listOfDetails = new ArrayList<>();
//    	MediaDropDetails md = new MediaDropDetails();
//    	md.setDropLocation(Location.builder().latitude(44.848742).longitude(-91.569357).build());
//    	
//    	
//    	List<RecipientAndPickupQuantity> listORecipientAndPickupQuantity = new ArrayList<>();
//    	RecipientAndPickupQuantity RaQ = RecipientAndPickupQuantity.builder().recipientId(1).quantityToPickup(1).build();
//    	listORecipientAndPickupQuantity.add(RaQ);
//    	
//    	md.setRecipientsAndPickupQuantities(listORecipientAndPickupQuantity);
//    	
//    	listOfDetails.add(md);
//    	
//    	
//    	mediaDropRequest.setMediaDropDetails(listOfDetails);
//    	dropMedia(mediaDropRequest);
    	
    	
    	Location userLocation = Location.builder()
                .latitude(latitude)
                .longitude(longitude).build();
        List<DropicMediaLocation> allMediaLocations = mediaLocationRepository.findAll();
        List<NearbyMediaResponse> nearbyMediaResponse = new ArrayList<>();


    	System.out.println("THERE ARE " + allMediaLocations.size() + " media Locations");
        // go through each media location
        for(DropicMediaLocation mediaLocation: allMediaLocations) {
            // get recipient at this location
            DropicMediaRecipientQuantity userRecipientAndQuantity = mediaRecipientRepository.getRecipientAndQuantityAtLocation(userID, mediaLocation.getId());

            if (userRecipientAndQuantity == null) {
                // the specific user could not be found, try to see if this media is available to everyone
                userRecipientAndQuantity = mediaRecipientRepository.getRecipientAndQuantityAtLocation(0, mediaLocation.getId());
            
            } else {
                System.out.println("GOT USER WITH QUANTITY");
            }

            DropicUserPickedUpMedia userPickedUpMedia = mediaPickUpRepository.getUserWithMedia(userID, mediaLocation.getMediaId());
            if (userRecipientAndQuantity != null &&
                    userPickedUpMedia == null &&
                    userRecipientAndQuantity.getQuantityToPickup() > 0) {
                // the user:
                // is a recipient of this media (either for the user or for everyone)
                // has not already picked up this media
                // has quantity to pick up
                DropicMedia media = mediaRepository.getOne(mediaLocation.getMediaId());
                System.out.println("is rec");
                System.out.println("userhasmedia : " + !(userPickedUpMedia == null));
                System.out.println("has quantity:  " + (userRecipientAndQuantity.getQuantityToPickup() > 0));


                // nearby media location
                Boolean isNearby = isUserInsideMediaLocation(userLocation, mediaLocation);
                System.out.println("user is nearby : " + isNearby);
                System.out.println("user lat : " + userLocation.getLatitude());
                System.out.println("user long : " + userLocation.getLongitude());
                System.out.println("media lat : " + mediaLocation.getLatitude());
                System.out.println("media long : " + mediaLocation.getLongitude());
                nearbyMediaResponse.add(NearbyMediaResponse.builder()
                        .mediaId(media.getId())
                        .mediaLocationId(mediaLocation.getId())
                        .longitude(mediaLocation.getLongitude())
                        .latitude(mediaLocation.getLatitude())
                        .isNearby(isNearby)
                        .name(media.getName()).build());
            }

        }
        return nearbyMediaResponse;
    }

    public DropicMedia grabMedia(Integer mediaID, Integer mediaLocationID, Integer userID, Location userLocation) {
        // grab the media that the user wants
        DropicMedia requestedMedia = mediaRepository.getOne(mediaID);
        DropicMediaLocation requestedMediaLocation = mediaLocationRepository.getOne(mediaLocationID);
        DropicUserPickedUpMedia userPickedUpMedia = mediaPickUpRepository.getUserWithMedia(userID, mediaID);

        if (requestedMedia.getId() == requestedMediaLocation.getMediaId()) {
            // the media location does exist for this media

            // get recipient at this location
            DropicMediaRecipientQuantity userRecipientAndQuantity = mediaRecipientRepository.getRecipientAndQuantityAtLocation(userID, mediaLocationID);

            if(userRecipientAndQuantity == null) {
                // the specific user could not be found, try to see if this media is available to everyone
                userRecipientAndQuantity = mediaRecipientRepository.getRecipientAndQuantityAtLocation(0, mediaLocationID);
            }

            if(userRecipientAndQuantity != null &&
                    userPickedUpMedia == null &&
                    isUserInsideMediaLocation(userLocation, requestedMediaLocation) &&
                    userRecipientAndQuantity.getQuantityToPickup() > 0) {
                // the user:
                // is a recipient of this media (either for the user or for everyone)
                // has not already picked up this media
                // nearby media's location
                // has quantity to pick up
                pickupMedia(userID, mediaID, userRecipientAndQuantity);
                return requestedMedia;
            }
        }

        return null;
    }
    
    public List<DropicMedia> getAllUsersMedia(Integer userID) {
    	List<DropicMedia> allUsersMedia = new ArrayList<>();
    	List<DropicUserPickedUpMedia> usersPickedUpMedia = mediaPickUpRepository.getAllUsersPickedUpMedia(userID);
    	for(DropicUserPickedUpMedia pickedMedia : usersPickedUpMedia) {
    		DropicMedia dm = mediaRepository.getOne(pickedMedia.getId());
    		allUsersMedia.add(dm);
    	}
    	return allUsersMedia;
    }

    public void dropMedia(MediaDropRequest mediaDropRequest) {
        UUID randomImagePath = UUID.randomUUID();

        // save image in amazon s3
        //uploadImage(mediaDropRequest, mediaDropRequest.getUserID() + "/" + randomImagePath);

        // create the media object to store in the database
        DropicMedia dropicMedia = DropicMedia.builder()
                .mediaPath(randomImagePath.toString())
                .droppedDate(mediaDropRequest.getDroppedDate())
                .message(mediaDropRequest.getMessage())
                .name(mediaDropRequest.getNameOfMedia())
                .userDropperID(mediaDropRequest.getUserID())
                .pickupDateDeadline(mediaDropRequest.getPickupDateDeadline()).build();

//        if( isAValidDrop(dropicMedia) ) {
//            return;
//        }

        DropicMedia savedDropicMedia = mediaRepository.save(dropicMedia);
        System.out.println(savedDropicMedia.getId());
        // Create the media locations and recipient-quantity at each location
        if (mediaDropRequest.getIsRandomDrop()){
            // create random drops
            Location userCurrentLocation = mediaDropRequest.getMediaDropDetails().get(0).getDropLocation();
            final int amountOfCoordinatesToCreate = 3;
            Random r = new Random();

            for (int i = 0; i < amountOfCoordinatesToCreate; i++) {
                // create coordinates that are in the vicinity of userCurrentLocation
                Double randomLatitude = userCurrentLocation.getLatitude();
                Double randomLongitude = userCurrentLocation.getLongitude();

                DropicMediaLocation dropicMediaLocation = new DropicMediaLocation().builder()
                        .mediaId(savedDropicMedia.getId())
                        .latitude(randomLatitude)
                        .longitude(randomLongitude).build();

                DropicMediaLocation savedDropicMediaLocation = mediaLocationRepository.save(dropicMediaLocation);

                // each location has the same recipient-quantity list
                for (RecipientAndPickupQuantity recipientAndPickupQuantity : mediaDropRequest.getMediaDropDetails().get(0).getRecipientsAndPickupQuantities()) {
                    int quantityToDrop = recipientAndPickupQuantity.getQuantityToPickup();
                    if (recipientAndPickupQuantity.getRecipientId() != 0 && quantityToDrop > 1) {
                        // the dropper wants to drop to a specific recipient but the recipient shouldn't have a quantity more than one
                        quantityToDrop = 1;
                    }

                    DropicMediaRecipientQuantity recipientAndQuantityForMedia = new DropicMediaRecipientQuantity().builder()
                            .mediaLocationId(savedDropicMediaLocation.getId())
                            .recipientUserId(recipientAndPickupQuantity.getRecipientId())
                            .quantityToPickup(quantityToDrop).build();
                    mediaRecipientRepository.save(recipientAndQuantityForMedia);

                }
            }
        } else {
            // regular drop
            // specified location and users at each location
            for (MediaDropDetails mediaLocationAndRecipients : mediaDropRequest.getMediaDropDetails()) {
                // go through each location and save the coordinates
                DropicMediaLocation dropicMediaLocation = new DropicMediaLocation().builder()
                        .mediaId(savedDropicMedia.getId())
                        .latitude(mediaLocationAndRecipients.getDropLocation().getLatitude())
                        .longitude(mediaLocationAndRecipients.getDropLocation().getLongitude()).build();

                DropicMediaLocation savedDropicMediaLocation = mediaLocationRepository.save(dropicMediaLocation);

                // each location has its own recipients and quantity list
                for (RecipientAndPickupQuantity recipientAndPickupQuantity: mediaLocationAndRecipients.getRecipientsAndPickupQuantities()) {
                    // go through each recipient and record their id and the quantity available

                    int quantityToDrop = recipientAndPickupQuantity.getQuantityToPickup();
                    if (recipientAndPickupQuantity.getRecipientId() != 0 && quantityToDrop > 1) {
                        // the dropper wants to drop to a specific recipient but the recipient shouldn't have a quantity more than one
                        quantityToDrop = 1;
                    }
                    
                    DropicMediaRecipientQuantity recipientAndQuantityForMedia = new DropicMediaRecipientQuantity().builder()
                                .mediaLocationId(savedDropicMediaLocation.getId())
                                .quantityToPickup(quantityToDrop)
                                .recipientUserId(recipientAndPickupQuantity.getRecipientId()).build();

                    
                    DropicMediaRecipientQuantity saved = mediaRecipientRepository.save(recipientAndQuantityForMedia);
                    System.out.println("USER BEING SAVED: " + saved.getRecipientUserId());
                }
            }
        }
    }

    private void pickupMedia(Integer userId, Integer mediaId, DropicMediaRecipientQuantity recipientAndQuantity) {

        // decrease quantity for that user at that location
        recipientAndQuantity.setQuantityToPickup(recipientAndQuantity.getQuantityToPickup()-1);
        mediaRecipientRepository.save(recipientAndQuantity);

        // add user and media to pick up table on today's date
        DropicUserPickedUpMedia userPickedUpMedia = new DropicUserPickedUpMedia().builder()
                .mediaID(mediaId)
                .userID(userId)
                .pickedUpDate(new Date()).build();

        mediaPickUpRepository.save(userPickedUpMedia);

    }

    private boolean isUserInsideMediaLocation(Location userLocation, DropicMediaLocation mediaLocation){
        return calculateDistanceBetweenCoordinates(userLocation.getLatitude(),
                userLocation.getLongitude(),
                mediaLocation.getLatitude(),
                mediaLocation.getLongitude(), "M") < 5;
    }

    private boolean isAValidDrop(DropicMedia dropicMedia) {
        // validate name, dates, message, image content
        return dropicMedia.getName().length() > 0;
    }

    private static double calculateDistanceBetweenCoordinates(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
            System.out.println("DISTANCE: " + dist);
            return (dist);
        }
    }


    private void uploadImage(MediaDropRequest droppedMedia, String imagePath) {
        // async upload media bytes' to amazon S3 at imagePath.
    }
}
