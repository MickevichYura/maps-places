package mickevichyura.github.com.mapsplaces;

import java.io.Serializable;

public class Place implements Serializable {

    private String id;

    private String name;

    private String address;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Place(String name, String address, String id) {
        this.name = name;
        this.address = address;
        this.id = id;

    }
}
