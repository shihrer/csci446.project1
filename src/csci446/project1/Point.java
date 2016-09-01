package csci446.project1;

import java.util.ArrayList;

/**
 * Created by cetho on 9/1/2016.
 */
public class Point {
    //Store the coordinates of this point.
    final private float x;
    final private float y;

    //Store any connections this point has to other points.
    private ArrayList<Point> connectedPoints;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;

        this.connectedPoints = new ArrayList<Point>();
    }

    public float x() {
        return this.x;
    }

    public float y() {
        return this.y;
    }

    public void connect(Point point) {
        //Add this point to the connection list of the other point in the pair.
        point.addPointToConnectionList(this);
        //Add the other point to this list so that the connection is consistent both ways.
        this.addPointToConnectionList(point);
    }

    private void addPointToConnectionList(Point point) {
        // Use the connect function to properly connect both points.
        this.connectedPoints.add(point);
    }

    public void printPoint() {
        System.out.println("(" + this.x + ", " + this.y + ")");
    }
}
