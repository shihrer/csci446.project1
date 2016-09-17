package csci446.project1.GraphSystem;

import java.awt.geom.Line2D;

/**
 * Created by cetho on 9/3/2016.
 */
public class Connection {
    final public Point point1;
    final public Point point2;

    public Connection(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public boolean checkForCollision(Connection intersectingConnection) {

        // Made after learned about this:
        // http://stackoverflow.com/questions/16333650/how-to-check-whether-2-lines-segments-intersect

        //Line2D line1 = new Line2D.Double(this.point1.x, this.point1.y, this.point2.x, this.point2.y);
        //Line2D line2 = new Line2D.Double(intersectingConnection.point1.x, intersectingConnection.point1.y, intersectingConnection.point2.x, intersectingConnection.point2.y);

        //This might be counting the end of the lines.
        //return line1.intersectsLine(line2)

        // New method produces more reasonable numbers

        //Based off the solution on Stack Overflow: http://stackoverflow.com/a/25831720
        return intersect(this.point1, this.point2, intersectingConnection.point1, intersectingConnection.point2);
    }

    //Based off the solution on Stack Overflow: http://stackoverflow.com/a/25831720
    public static int orientation(Point p, Point q, Point r) {
        double val = (q.y - p.y) * (r.x - q.x)
                - (q.x - p.x) * (r.y - q.y);

        if (val == 0.0)
            return 0; // colinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    //Based off the solution on Stack Overflow: http://stackoverflow.com/a/25831720
    public static boolean intersect(Point p1, Point q1, Point p2, Point q2) {

        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4)
            return true;

        return false;
    }

}
