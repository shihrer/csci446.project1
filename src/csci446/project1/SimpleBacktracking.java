package csci446.project1;

import csci446.project1.GraphSystem.Graph;
import csci446.project1.GraphSystem.Point;

/**
 * Created by cetho on 9/1/2016.
 */
public class SimpleBacktracking {

    public boolean success;

    public int iterations;
    private int[] colors;

    private int numColors;

    private Point[] points;

    public SimpleBacktracking(int numColors, Graph graph) {
        // Initialize variables
        this.numColors = numColors;

        this.points = graph.points;

        colors = new int[points.length];
        // Set all colors to 0; 0 means uncolored.
        for (int color : colors) {
            color = 0;
        }

        boolean result = this.graphColoringRecursive(0);

        if(result) {
            System.out.println();
            success = true;
            System.out.println("\tSimpleBacktracking: Successfully found solution with " + numColors + " colors.");
            //System.out.println();
            //this.printColors();
        } else {
            System.out.println();
            success = false;
            System.out.println("\tSimpleBacktracking: Failed to find solution with " + numColors + " colors.");
        }
    }

    private boolean graphColoringRecursive(int currentPoint) {
        // Base case
        if(currentPoint == points.length) {
            //This is out of bounds, which means we've colored all the points!
            // We're done here.
            if(Main.verbose) {
                System.out.println("\t\tSimpleBacktracking: Reached base case.");
            }
            return true;
        }
        // Attempt to color this point.
        for(int color = 1; color <= this.numColors; color++) {
            if(Main.verbose) {
                System.out.println("\t\tSimpleBacktracking: Checking P" + currentPoint + " for possible colors.");
            }
            if(canColor(currentPoint, color)) {
                // We can color this point. Lets do it.
                if(Main.verbose) {
                    System.out.println("\t\tSimpleBacktracking: Coloring P" + currentPoint + " color " + color);
                }
                colors[currentPoint] = color;
                iterations++;
                if(iterations >= 40000) {
                    //Too many iterations.
                    if(Main.verbose) {
                        System.out.println("\t\tSimpleBacktracking: Reached 40000 iterations. Giving up.");
                    }
                    return false;
                }
                // Try to color the next point in the list.
                if(graphColoringRecursive(currentPoint + 1)) {
                    return true;
                }
                if(Main.verbose) {
                    System.out.println("\t\tSimpleBacktracking: Couldn't color under P" + currentPoint + " with color " + color + ". Trying a new one.");
                }
                // It failed to color. :( Lets remove our color because this is a bad path
                colors[currentPoint] = 0;
            }
            if(Main.verbose) {
                System.out.println("\t\tSimpleBacktracking: P" + currentPoint + " can't be colored with color " + color + ".");
            }
        }
        // Ran out of colors to try. We've failed. Hopefully our friends higher in the chain can color the graph.
        if(Main.verbose) {
            System.out.println("\t\tSimpleBacktracking: Out of colors to try at P" + currentPoint + ". Backtracking.");
        }
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

    private void printColors() {
        for (int i = 0; i < points.length; i++) {
            System.out.println("SimpleBacktracking: P" + i + ": Color " + this.colors[i]);
        }
    }
}
