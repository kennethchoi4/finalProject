import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class MinerNotFull extends MinerEntity{

    public MinerNotFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, resourceCount);
    }


    /*
    public Point position() {return position;}

    public void setPosition(Point position) {this.position = position;}

    public int actionPeriod() {return actionPeriod;}

    public int getAnimationPeriod() {return this.animationPeriod;}
    */

    //public void nextImage() {this.imageIndex = (this.imageIndex + 1) % this.images.size();}

    //public PImage getCurrentImage() {return (this.images.get((this).imageIndex));}

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                findNearest(world, this.position(), Ore.class);

        if (!notFullTarget.isPresent() || !this.moveToNotFull(world,
                notFullTarget.get(),
                scheduler)
                || !this.transformNotFull(world, scheduler, imageStore))
        {

            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    this.actionPeriod());
        }

    }


    /*
    private static Optional<Entity> findNearest(
            WorldModel world, Point pos, Class kind)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.entities()) {
            if (entity.getClass() == kind) {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

    private static Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = Point.distanceSquared(nearest.position(), pos);

            for (Entity other : entities) {
                int otherDistance = Point.distanceSquared(other.position(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
    */


    private boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit()) {
            Entity miner = Factory.createMinerFull(this.getId(), this.getResourceLimit(),
                    this.position(), this.actionPeriod(),
                    this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            ((ActiveEntity)miner).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    private boolean moveToNotFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(this.position(), target.position())) {
            this.addResourceCount(1);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            Point nextPos = this.nextPositionMiner(world, target.position());

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


    /*
    private Point nextPositionMiner(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position().x);
        int vert = Integer.signum(destPos.y - this.position().y);

        Point newPos = new Point(this.position().x + horiz, this.position().y + vert);

        if (world.isOccupied(newPos)) {

            newPos = new Point(this.position().x, this.position().y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = new Point(this.position().x + horiz, this.position().y);
                if (horiz == 0 || world.isOccupied(newPos))
                {
                    newPos = this.position();
                }
            }

        }

        return newPos;
    }
     */

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
        scheduler.scheduleEvent(this,
                Factory.createAnimationAction(this, 0),
                this.getAnimationPeriod());
    }

}
