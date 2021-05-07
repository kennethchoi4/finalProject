import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public interface Entity
{
    public Point position();
    public void setPosition(Point p);
    public void nextImage();
    public PImage getCurrentImage();
}
