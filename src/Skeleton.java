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
        Optional<Entity> skeletonTarget =
                findNearest(world, this.position(), MinerNotFull.class);
        long nextPeriod = this.actionPeriod();


        if (skeletonTarget.isPresent()) {
            Point pos = skeletonTarget.get().position();

            if (this.moveToSkeleton(world, skeletonTarget.get(), scheduler)) {
                world.removeEntity(skeletonTarget.get());
                scheduler.unscheduleAllEvents(skeletonTarget.get());
                Skeleton skeleton = Factory.createSkeleton("skeleton", pos, imageStore.getImageList("skeleton"), 5, 6);
                world.addEntity(skeleton);
                skeleton.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    private boolean moveToSkeleton(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(this.position(), target.position())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            Point nextPos = this.nextPositionSkeleton(world, target.position());

            if (!this.position().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
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