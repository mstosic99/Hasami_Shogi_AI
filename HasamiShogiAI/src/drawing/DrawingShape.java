package drawing;

import java.awt.Graphics2D;

public interface DrawingShape {
	
    boolean contains(Graphics2D g2, double x, double y);
    void adjustPosition(double dx, double dy);
    void draw(Graphics2D g2);
}
