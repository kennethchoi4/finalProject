import processing.core.PImage;
import java.util.*;

public class Ore extends ActiveEntity{
    private int animationPeriod;

    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;

    private static final Random rand = new Random();

    public Ore(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod)
    {
        super(id, position, images, actionPeriod);

    }


    //public Point position() { return position;}

    //public void setPosition(Point position) { this.position = position; }

    //public int actionPeriod() { return actionPeriod; }

    //public int getAnimationPeriod() {return animationPeriod;}


    //public void nextImage() { this.imageIndex = (this.imageIndex + 1) % this.images.size(); }

    //public PImage getCurrentImage() { return (this.images.get((this).imageIndex)); }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Point pos = this.position();
        System.out.println(pos);
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        System.out.println("here");
        OreBlob blob = Factory.createOreBlob(this.getId() + BLOB_ID_SUFFIX, pos,
                this.actionPeriod() / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN + rand.nextInt(
                        BLOB_ANIMATION_MAX
                                - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }

    /*
    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                this.actionPeriod());
    }
     */

}
