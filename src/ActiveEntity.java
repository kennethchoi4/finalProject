/*import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity
{
    private int animationPeriod;
    private int imageIndex;
    private List<PImage> images;

    public AnimatedEntity(int animationPeriod)
    {
        this.animationPeriod = animationPeriod;
    }

    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }
}
*/




//OLD INTERFACE IMPLEMENTATION BEFORE ABSTRACT CLASS

import processing.core.PImage;

import java.util.List;

public abstract class ActiveEntity extends Entity {

    private int actionPeriod;

    public ActiveEntity(String id, Point position, List<PImage> images, int actionPeriod){
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                this.actionPeriod());
    }

    public int actionPeriod() { return this.actionPeriod;}
}


/*
OLD IMPLEMENTATION BEFORE ABSTRACT CLASS

public interface ActiveEntity extends Entity{

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    //public int getAnimationPeriod();

    //public void nextImage();
}
*/
