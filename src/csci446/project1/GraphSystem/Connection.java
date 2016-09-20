package csci446.project1.GraphSystem;

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
        double x1 = this.point1.x;
        double x2 = this.point2.x;
        double y1 = this.point1.y;
        double y2 = this.point2.y;

        double x3 = intersectingConnection.point1.x;
        double x4 = intersectingConnection.point2.x;
        double y3 = intersectingConnection.point1.y;
        double y4 = intersectingConnection.point2.y;

        /*
         * Collision detection algorithm based off:
         * http://stackoverflow.com/a/16314158
         */

        //Check for vertical lines
        if(x1 == x2 || x3 == x4) {
            if(x1 == x2 && x3 == x4) {
                //Both vertical
                if(x1 != x3) {
                    //Not at the same x.
                    return false;
                }
                //Check if y coordinates over lap.
                double max1 = Math.max(y1, y2);
                double min1 = Math.min(y1, y2);
                double max2 = Math.max(y3, y4);
                double min2 = Math.min(y3, y4);

                if(max1 <= min2 && max1 < max2) {
                    //No intersect, l2 above l1
                    return false;
                }
                else if(max2 <= min1 && max2 < min1) {
                    //l1 above l2
                    return false;
                }
                else {
                    //If its not above and its not below, its in between.
                    return true;
                }
            }
            //Only one is vertical
            if(x1 == x2) {
                // Line 1 vertical
                double a2 = (y4 - y3)/(x4 - x3);
                double b2 = y3 -a2*x3;

                double xint = a2*x1 + b2;

                if(xint < Math.max(y1, y2) && xint > Math.min(y1, y2)) {
                    //Intersects somewhere between the vertical line
                    return true;
                }
                // No intersection
                return false;
            }
            else {
                //Line 2 vertical
                double a1 = (y2-y1)/(x2-x1);
                double b1 = y1 - a1*x1;

                double xint = a1*x3 + b1;

                if(xint < Math.max(y3, y4) && xint > Math.min(y3, y4)) {
                    return true;
                }
                return false;
            }
        }
        //Neither lines vertical.
        double a1 = (y2-y1)/(x2-x1);
        double b1 = y1 - a1*x1;
        double a2 = (y4-y3)/(x4-x3);
        double b2 = y3 - a2*x3;

        if(a1 == a2) {
            //The lines are parallel.
            if(b1 == b2) {
                //Same y intercept. Lets cry in a corner. When that's done we need to check if there is overlap.
                double max1 = Math.max(x1, x2);
                double min1 = Math.min(x1, x2);
                double max2 = Math.max(x3, x4);
                double min2 = Math.min(x3, x4);

                if(max1 <= min2 && max1 < max2) {
                    //No intersect, l2 right of l1
                    return false;
                }
                else if(max2 <= min1 && max2 < min1) {
                    //l1 eight of l2
                    return false;
                }
                else {
                    //If its not right and its not left, its in between.
                    return true;
                }
            }
            // Parallel, different intercepts. We're done.
            return false;
        }
        // Lines not parallel. Thank god.
        double x0 = -(b1-b2)/(a1-a2);
        if(Math.min(x1, x2) < x0 && x0 < Math.max(x1, x2) && Math.min(x3, x4) < x0 && x0 < Math.max(x3, x4)) {
            //x0 is somewhere between both lines. This is an intersection.
            return true;
        }
        // Home free.
        return false;
    }

}
