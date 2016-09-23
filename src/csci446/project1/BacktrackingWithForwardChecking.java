package csci446.project1;

import csci446.project1.GraphSystem.Graph;
import csci446.project1.GraphSystem.Point;

/**
 * Created by cetho on 9/1/2016.
 */
public class BacktrackingWithForwardChecking {

    public int iterations;
    public boolean success;

    private int[] colors;

    private int numColors;

    private Point[] points;

    private boolean[][] blockedColors;

    public BacktrackingWithForwardChecking(int numColors, Graph graph) {
        // Initialize variables
        this.numColors = numColors;

        this.points = graph.points;

        colors = new int[points.length];
        // Set all colors to 0; 0 means uncolored.
        for (int color : colors) {
            color = 0;
        }

        // Initialize blocked colors; false = available.
        this.blockedColors = new boolean[points.length][numColors];

        boolean result = this.graphColoringRecursive(0);

        if(result) {
            System.out.println();
            success = true;
            System.out.println("\tBacktrackingWithForwardChecking: Successfully found solution with " + numColors + " colors.");
            System.out.println();
            //this.printColors();
        } else {
            System.out.println();
            success = false;
            System.out.println("\tBacktrackingWithForwardChecking: Failed to find solution with " + numColors + " colors.");
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
                iterations++;
                // Forwardchecking : Block off the color for adjacent points.
                blockedColors[currentPoint][color - 1] = true; //Block this color for ourselves, just to say we chose it.
                for ( Point point: this.points[currentPoint].connectedPoints) {
                    blockedColors[point.id][color - 1] = true;
                }
                //Make sure theres still legal moves.
                if(!legalValuesRemaining(currentPoint)) {
                    //Cant do this. Edit->Undo.
                    colors[currentPoint] = 0;
                    blockedColors[currentPoint][color - 1] = false;
                    for ( Point point: this.points[currentPoint].connectedPoints) {
                        blockedColors[point.id][color - 1] = false;
                    }
                } else {
                    // Try to color the next point in the list.
                    if(graphColoringRecursive(currentPoint + 1)) {
                        return true;
                    }
                    // It failed to color. :( Lets remove our color because this is a bad path
                    colors[currentPoint] = 0;
                    blockedColors[currentPoint][color - 1] = false;
                    for ( Point point: this.points[currentPoint].connectedPoints) {
                        blockedColors[point.id][color - 1] = false;
                    }
                }
            }
        }
        // Ran out of colors to try. We've failed. Hopefully our friends higher in the chain can color the graph.
        return false;
    }

    private boolean legalValuesRemaining(int lastAssigned) {
        for (int i = lastAssigned + 1; i < points.length; i++) {
            boolean foundFree = false;
            for(int j = 0; j < numColors; j++) {
                if(!blockedColors[i][j]) {
                    foundFree = true;
                }
            }
            if(!foundFree) {
                return false;
            }
        }
        return true;
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

    private void printColors() {
        for (int i = 0; i < points.length; i++) {
            System.out.println("BacktrackingWithForwardChecking: P" + i + ": Color " + this.colors[i]);
        }
    }
}
