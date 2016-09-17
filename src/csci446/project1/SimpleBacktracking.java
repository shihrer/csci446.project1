package csci446.project1;

import csci446.project1.GraphSystem.Graph;
import csci446.project1.GraphSystem.Point;

/**
 * Created by cetho on 9/1/2016.
 */
public class SimpleBacktracking {

    private int[] colors;

    private int numColors;

    private Graph graph;
    private Point[] points;

    public SimpleBacktracking(int numColors, Graph graph) {
        // Initialize variables
        this.numColors = numColors;

        this.graph = graph;
        this.points = graph.points;

        colors = new int[points.length];
        // Set all colors to 0; 0 means uncolored.
        for (int color : colors) {
            color = 0;
        }

        boolean result = this.graphColoringRecursive(0);

        if(result) {
            System.out.println("SimpleBacktracking: Successfully found solution with " + numColors + " colors.");
        } else {
            System.out.println("SimpleBacktracking: Failed to find solution with " + numColors + " colors.");
        }
    }

    private boolean graphColoringRecursive(int currentPoint) {
        // Base case
        if(currentPoint == points.length) {
            //This is out of bounds, which means we've colored all the points!
            // We're done here.
            return true;
        }
        // Attempt to color this point.
        for(int color = 1; color <= this.numColors; color++) {
            if(canColor(currentPoint, color)) {
                // We can color this point. Lets do it.
                colors[currentPoint] = color;
                // Try to color the next point in the list.
                if(graphColoringRecursive(currentPoint + 1)) {
                    return true;
                }
                // It failed to color. :( Lets remove our color because this is a bad path
                colors[currentPoint] = 0;
            }
        }
        // Ran out of colors to try. We've failed. Hopefully our friends higher in the chain can color the graph.
        return false;
    }

    private boolean canColor(int point, int color) {
        // Check each point connected to the point in question. If we've colored it the color "color" already, then
        // return false.
        for(Point checkPoint : points[point].connectedPoints) {
            if(colors[checkPoint.id] == color) {
                return false;
            }
        }
        return true;
    }
}
