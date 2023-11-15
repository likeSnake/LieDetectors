package net.instrument.free.utility.liedetectors.pojo;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Country {
    private Bitmap imageView;
    private String name;

    public Country(Bitmap imageView, String name) {
        this.imageView = imageView;
        this.name = name;
    }

    public Bitmap getImageView() {
        return imageView;
    }

    public void setImageView(Bitmap imageView) {
        this.imageView = imageView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
