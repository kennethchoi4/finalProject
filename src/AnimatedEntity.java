import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity{

    private int animationPeriod;

    public AnimatedEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod);

        this.animationPeriod = animationPeriod;
    }
    public int getAnimationPeriod() { return this.animationPeriod; }

    public void nextImage(){ this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size()); }

    //public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}






/*
OLD INTERFACE BEFORE ABSTRACT CLASS

public interface AnimatedEntity extends ActiveEntity{
    public int getAnimationPeriod();

    public void nextImage();
}
*/