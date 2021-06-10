import processing.core.PImage;
import java.util.List;
import java.util.LinkedList;
import java.util.function.Predicate;
import java.util.function.BiPredicate;
import java.util.Optional;
import java.util.Random;

public class LavaHound extends MovingEntity{
    public LavaHound(String id,
                     Point position,
                     List<PImage> images,
                     int actionPeriod,
                     int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public Point nextPositionLavaHound(WorldModel world, Point destPos) {
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
        Optional<Entity> lavaHoundTarget =
                findNearest(world, this.position(), Skeleton.class);

        if (lavaHoundTarget.isPresent())
        {
            Point pos = lavaHoundTarget.get().position();

            if (this.moveToLavaHound(world, lavaHoundTarget.get(), scheduler)) {
                world.removeEntity(lavaHoundTarget.get());
                scheduler.unscheduleAllEvents(lavaHoundTarget.get());
                //MinerNotFull miner = Factory.createMinerNotFull("miner", 4, pos, 5, 6, imageStore.getImageList("miner"));
                //world.addEntity(miner);
                //miner.scheduleActions(scheduler, world, imageStore);
            }
        }

            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    this.actionPeriod());
    }

    private boolean moveToLavaHound(
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
            Point nextPos = this.nextPositionLavaHound(world, target.position());

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
