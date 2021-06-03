import processing.core.PImage;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.BiPredicate;

public abstract class MinerEntity extends MovingEntity{
    private int resourceLimit;
    private int resourceCount;

    public MinerEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int resourceLimit, int resourceCount){

        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public int getResourceLimit() {return this.resourceLimit;}
    public int getResourceCount() {return this.resourceCount;}
    public void addResourceCount(int num) {this.resourceCount += num;}

    public Point nextPositionMiner(WorldModel world, Point destPos) {
        /*
        int horiz = Integer.signum(destPos.x - this.position().x);
        int vert = Integer.signum(destPos.y - this.position().y);

        Point newPos = new Point(this.position().x + horiz, this.position().y + vert);

        if (world.isOccupied(newPos)) {

            newPos = new Point(this.position().x, this.position().y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = new Point(this.position().x + horiz, this.position().y);
                if (horiz == 0 || world.isOccupied(newPos)) {
                    newPos = this.position();
                }
            }

        }

        return newPos;
        */


        Predicate<Point> params = (Point point) -> (!(world.isOccupied(point)) && world.withinBounds(point));
        BiPredicate<Point, Point> reach = Functions::adjacent;
        AStarPathingStrategy strat = new AStarPathingStrategy();

        List<Point> path = strat.computePath(this.position(), destPos, params, reach, PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() == 0) {return this.position();}

        return path.get(0);


    }


}
