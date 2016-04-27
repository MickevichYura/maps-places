package mickevichyura.github.com.mapsplaces.model;

import com.google.android.gms.location.places.Place;

import java.io.Serializable;

public class PlaceObject implements Serializable {

    private String id;

    private String name;

    private String address;

    private String websiteUri;

    private String phoneNumber;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsiteUri() {
        return websiteUri;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "PlaceObject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", websiteUri='" + websiteUri + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public PlaceObject(String name, String address, String id) {
        this.name = name;
        this.address = address;
        this.id = id;

    }

    public PlaceObject(Place place) {
        this.name = place.getName().toString();
        this.address = place.getAddress().toString();
        this.id = place.getId();
        this.phoneNumber = place.getPhoneNumber().toString();
        if (place.getWebsiteUri() != null)
            this.websiteUri = place.getWebsiteUri().toString();
        place.getPlaceTypes();
        place.getRating();
    }
}
