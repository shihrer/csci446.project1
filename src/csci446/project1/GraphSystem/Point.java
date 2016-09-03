package csci446.project1.GraphSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by cetho on 9/1/2016.
 */
public class Point {
    //Store the coordinates of this point.
    final public double x;
    final public double y;
    final public int id;
    final private Graph graph;
    private int currentListSpot;
    private Point[] possibleConnections;
    //Store any connections this point has to other points.
    private ArrayList<Point> connectedPoints;

    public Point(float x, float y, Graph graph, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.graph = graph;
        //Always start at the second closest point. We don't want to connect to ourselves.
        this.currentListSpot = 1;

        this.connectedPoints = new ArrayList<Point>();

    }

    public void setup() {
        //Setup to do after all points have been created.
        this.possibleConnections = graph.points.clone();
        this.sort();
    }

    public double distanceSquaredToPoint(Point point) {
        //Using that wonderful distance formula! Minus the square root. That's just a waste of CPU time.
        return Math.pow((point.x - this.x),2) + Math.pow((point.y - this.y),2);
    }

    public boolean canConnect(Point point) {
        Connection candidate = new Connection(this, point);
        for(Connection connection : graph.connections) {
            if(connection.checkForCollision(candidate)) {
                return false;
            }
        }
        return true;
    }

    private void connect(Point point) {
        //Add this point to the connection list of the other point in the pair.
        point.addPointToConnectionList(this);
        //Add the other point to this list so that the connection is consistent both ways.
        this.addPointToConnectionList(point);
        //Add it to the global list of connections to check against.
        this.graph.connections.add(new Connection(this, point));
    }

    private void addPointToConnectionList(Point point) {
        // Use the connect function to properly connect both points.
        this.connectedPoints.add(point);
    }

    public void printPoint() {
        System.out.print("(" + this.x + ", " + this.y + ")");
    }


    /*
     * Quick Sort Algorithm
     * Modified from http://www.java2novice.com/java-sorting-algorithms/quick-sort/
     * Make sure we cite it.
     */

    private void sort() {

        if (this.possibleConnections == null || this.possibleConnections.length == 0) {
            return;
        }
        quickSort(0, this.possibleConnections.length - 1);
    }

    private void quickSort(int lowerIndex, int higherIndex) {

        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        double pivot = this.possibleConnections[lowerIndex+(higherIndex-lowerIndex)/2].distanceSquaredToPoint(this);
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (this.possibleConnections[i].distanceSquaredToPoint(this) < pivot) {
                i++;
            }
            while (this.possibleConnections[j].distanceSquaredToPoint(this) > pivot) {
                j--;
            }
            if (i <= j) {
                exchangePoints(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j);
        if (i < higherIndex)
            quickSort(i, higherIndex);
    }

    private void exchangePoints(int i, int j) {
        Point temp = this.possibleConnections[i];
        this.possibleConnections[i] = this.possibleConnections[j];
        this.possibleConnections[j] = temp;
    }

    public boolean connectToNextPoint() {
        while (true) {
            if(this.currentListSpot >= this.possibleConnections.length) {
                //Out of points to try to connect to.
                return false;
            }
            if(this.canConnect(this.possibleConnections[this.currentListSpot])) {
                this.connect(this.possibleConnections[this.currentListSpot]);
                this.currentListSpot++;
                return true;
            }
            this.currentListSpot++;
        }
    }
}
