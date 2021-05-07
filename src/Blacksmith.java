import processing.core.PImage;
import java.util.*;

public class Blacksmith implements Entity{
    private String id;
    private Point position;
    private List<PImage> images;

    public Blacksmith(
            String id,
            Point position,
            List<PImage> images
            )
    {
        this.id = id;
        this.position = position;
        this.images = images;

    }


    public Point position() { return this.position;}
    public void setPosition(Point position) {
        this.position = position;
    }
    public void nextImage() { }
    public PImage getCurrentImage() { return images.get(0); }

}
