import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public abstract class Entity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(String id, Point position, List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public String getId() { return this.id; }

    public Point position(){ return this.position; }

    public List<PImage> getImages() { return this.images; }

    public int getImageIndex() {return this.imageIndex;}

    public PImage getCurrentImage() {return this.images.get(imageIndex);}

    public void setPosition(Point pos) {this.position = pos;}

    public void setImageIndex(int index) {this.imageIndex = index;}

}

/*
OLD INTERFACE IMPLEMENTATION BEFORE ABSTRACT CLASS

public interface Entity
{
    public Point position();
    public void setPosition(Point p);
    public void nextImage();
    public PImage getCurrentImage();
} */
