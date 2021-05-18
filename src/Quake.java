import processing.core.PImage;


import java.util.List;


public class Quake extends AnimatedEntity{
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    /*
    public Point position() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int actionPeriod() {
        return actionPeriod;
    }

    public int getAnimationPeriod() { return this.animationPeriod;}
    */

    //public void nextImage() {this.imageIndex = (this.imageIndex + 1) % this.images.size(); }

    //public PImage getCurrentImage() { return (this.images.get((this).imageIndex)); }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        /*
        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                this.actionPeriod());
         */
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this, Factory.createAnimationAction(this,
                QUAKE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }
}
