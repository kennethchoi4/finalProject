import processing.core.PImage;
import java.util.List;
import java.util.LinkedList;
import java.util.function.Predicate;
import java.util.function.BiPredicate;
import java.util.Optional;
import java.util.Random;

public class Skeleton extends MovingEntity{
    public Skeleton(String id,
                     Point position,
                     List<PImage> images,
                     int actionPeriod,
                     int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public Point nextPositionSkeleton(WorldModel world, Point destPos) {
        Predicate<Point> params = (Point point) -> (!(world.isOccupied(point)) && world.withinBounds(point));
        BiPredicate<Point, Point> reach = Functions::adjacent;
        AStarPathingStrategy strat = new AStarPathingStrategy();

        List<Point> path = strat.computePath(this.position(), destPos, params, reach, PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() == 0) {return this.position();}

        return path.get(0);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> minerTarget =
                findNearest(world, this.position(), MinerEntity.class);

        if (!minerTarget.isPresent())
        {

            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    this.actionPeriod());
        }
    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world,
                                ImageStore imageStore)
    {
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this,
                Factory.createAnimationAction(this, 0),
                this.getAnimationPeriod());
    }

}