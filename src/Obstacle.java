import processing.core.PImage;


import java.util.List;


public class Obstacle extends Entity{

    public Obstacle(
            String id,
            Point position,
            List<PImage> images)
    {
        super(id, position, images);
    }

    //public Point position() { return position;}

    //public void setPosition(Point position) { this.position = position; }

    //public PImage getCurrentImage() { return (this.getImages().get((this).imageIndex)); }


}
