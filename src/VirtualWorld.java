import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String LOAD_FILE_NAME = "world.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    public void mousePressed()
    {
        Point pressed = mouseToPoint(mouseX, mouseY);

        ArrayList<Point> points = new ArrayList<>();
        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++) {
                Point lavaTile = new Point(pressed.x + i, pressed.y + j);
                if (world.withinBounds(lavaTile))
                {
                    world.setBackground(lavaTile, new Background("lava", imageStore.getImageList("lava")));
                    points.add(lavaTile);
                }

                if (world.isOccupied(lavaTile)) {
                    Optional<Entity> occupant = world.getOccupant(lavaTile);
                    if (occupant.get() instanceof Entity && !(occupant.get() instanceof MinerEntity) && !(occupant.get() instanceof Skeleton) && !(occupant.get() instanceof LavaHound)) {
                        world.removeEntity(occupant.get());
                        scheduler.unscheduleAllEvents(occupant.get());
                    }
                    if (occupant.get() instanceof MinerEntity){
                        Point pos = occupant.get().position();
                        world.removeEntity(occupant.get());
                        scheduler.unscheduleAllEvents(occupant.get());
                        Skeleton skeleton = Factory.createSkeleton("skeleton", pos, imageStore.getImageList("skeleton"), 800, 5);
                        world.addEntity(skeleton);
                        skeleton.scheduleActions(scheduler, world, imageStore);
                    }
                }
            }
        }

        //Creates lavahound
        Random rand = new Random();
        int pointRand = rand.nextInt(10);
        int index1 = rand.nextInt(points.size());
        Point p1 = points.get(index1);
        p1 = new Point(p1.x + pointRand, p1.y + pointRand);
        while (world.isOccupied(p1) && world.getOccupant(p1).get() instanceof MinerEntity){
            index1 = rand.nextInt(points.size());
            p1 = points.get(index1);
            p1 = new Point(p1.x + pointRand, p1.y + pointRand);
        }
        int index2 = rand.nextInt(points.size());
        Point p2 = points.get(index2);
        p2 = new Point(p2.x + pointRand, p2.y + pointRand);
        while (world.isOccupied(p2) && world.getOccupant(p2).get() instanceof MinerEntity && p1.equals(p2)){
            index2 = rand.nextInt(points.size());
            p2 = points.get(index2);
            p2 = new Point(p2.x + pointRand, p2.y + pointRand);
        }
        LavaHound hound1 = Factory.createLavaHound("lavaHound", p1, imageStore.getImageList("lavahound"), 1300, 5);
        world.addEntity(hound1);
        hound1.scheduleActions(scheduler, world, imageStore);

        LavaHound hound2 = Factory.createLavaHound("lavaHound", p2, imageStore.getImageList("lavahound"), 1300, 5);
        world.addEntity(hound2);
        hound2.scheduleActions(scheduler, world, imageStore);
    }

    private Point mouseToPoint(int x, int y) { return new Point(mouseX/TILE_WIDTH + view.getViewport().col(), mouseY/TILE_HEIGHT + view.getViewport().row());}

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                              imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            Functions.load(in, world, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.entities()) {
            if (entity instanceof ActiveEntity){
                ((ActiveEntity)entity).scheduleActions(scheduler, world, imageStore);
            }

        }
    }

    public static void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
