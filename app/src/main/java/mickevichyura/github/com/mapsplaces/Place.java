package mickevichyura.github.com.mapsplaces;

import android.graphics.drawable.Drawable;

public class Place {

    private String id;

    private String name;

    private String address;

    private Drawable photoDrawable;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Drawable getPhotoDrawable() {
        return photoDrawable;
    }


    public Place(String name, String age, Drawable photoDrawable, String id) {
        this.name = name;
        this.address = age;
        this.photoDrawable = photoDrawable;
        this.id = id;

    }
}
