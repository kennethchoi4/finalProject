import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein extends ActiveEntity{


    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 9000;
    private static final int ORE_CORRUPT_MAX = 20000;


    private static final Random rand = new Random();

    public Vein(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }


    // public Point position() { return position;}

   // public void setPosition(Point position) { this.position = position; }

    //public int actionPeriod() { return actionPeriod; }

    //public int getAnimationPeriod() { return 0;}

    //public void nextImage() { this.imageIndex = (this.imageIndex + 1) % this.images.size(); }

    // public PImage getCurrentImage() { return (this.images.get((this).imageIndex)); }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.position());

        if (openPt.isPresent()) {
            Ore ore = Factory.createOre(ORE_ID_PREFIX + this.getId(), openPt.get(),
                    ORE_CORRUPT_MIN + rand.nextInt(
                            ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(Functions.oreKey()));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                this.actionPeriod());
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
