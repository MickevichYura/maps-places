package mickevichyura.github.com.mapsplaces;

import android.graphics.drawable.Drawable;

public class Place {

    public String name;
    public String address;
    public Drawable photoDrawable;

    Place(String name, String age, Drawable photoDrawable) {
        this.name = name;
        this.address = age;
        this.photoDrawable = photoDrawable;
    }
}
