package mickevichyura.github.com.mapsplaces.model;

import android.graphics.Bitmap;

public class PhotoObject
{
    private Bitmap photo;
    private String author;

    public PhotoObject(Bitmap photo, String author)
    {
        this.photo = photo;
        this.author = author;
    }

    public Bitmap getPhoto()
    {
        return photo;
    }

    public void setPhoto(Bitmap photo)
    {
        this.photo = photo;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }
}