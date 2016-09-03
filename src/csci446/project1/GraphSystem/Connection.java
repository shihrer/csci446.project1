package csci446.project1.GraphSystem;

/**
 * Created by cetho on 9/3/2016.
 */
public class Connection {
    final public Point point1;
    final public Point point2;
    final private double slope;
    final private double intercept;
    final private double[] collisionBox;

    public Connection(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
        //Calculate the slope.
        this.slope = (point2.y - point1.y)/(point2.x-point1.x);
        this.intercept = point1.y - (this.slope * point1.x);

        double minx = (point1.x < point2.x) ? point1.x : point2.x;
        double maxx = (point1.x < point2.x) ? point2.x : point2.x;
        double miny = (point1.y < point2.y) ? point1.y : point2.y;
        double maxy = (point1.y < point2.y) ? point2.y : point2.y;

        this.collisionBox = new double[] {minx,maxx,miny,maxy};
    }

    public boolean checkForCollision(Connection intersectingConnection) {
        //Check for identical connections.
        if(this.point1 == intersectingConnection.point1 && this.point2 == intersectingConnection.point2) {
            return true;
        }
        //Check for identical lines where the points may be reversed.
        if(this.point2 == intersectingConnection.point1 && this.point1 == intersectingConnection.point2) {
            return true;
        }
        //Find the intersection of both connections.
        double intersectx = (intersectingConnection.intercept - this.intercept)/(this.slope - intersectingConnection.slope);
        double intersecty = (this.slope*intersectx) + this.intercept;
        //Check if that intersect is within both collision boxes. If it is, the true connections intersect.
        if(intersectx >= collisionBox[0] && intersectx <= collisionBox[1]) {
            if(intersecty >= collisionBox[2] && intersecty <= collisionBox[3]) {
                //The intersect is within the first box. Check the second.
                if(intersectx >= intersectingConnection.collisionBox[0] && intersectx <= intersectingConnection.collisionBox[1]) {
                    if(intersecty >= intersectingConnection.collisionBox[2] && intersecty <= intersectingConnection.collisionBox[3]) {
                        //These connections collide and are invalid.
                        return true;
                    }
                }
            }
        }

        return false;
    }


}
