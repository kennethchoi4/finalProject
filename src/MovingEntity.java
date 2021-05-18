import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class MovingEntity extends AnimatedEntity{

    public MovingEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public static Optional<Entity> findNearest(
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

    public static Optional<Entity> nearestEntity(
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
}
