import processing.core.PImage;


import java.util.List;


public class Obstacle implements Entity{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Obstacle(
            String id,
            Point position,
            List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public Point position() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }


    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public PImage getCurrentImage() {
        return (this.images.get((this).imageIndex));
    }


}
