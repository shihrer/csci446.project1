package csci446.project1;

import csci446.project1.GraphSystem.Graph;
import csci446.project1.GraphSystem.Point;

import java.util.*;

class BacktrackingMAC {
    private Graph graph;
    private int k;
    boolean success = false;
    int iterations = 0;
    //Initialize coloring and domain tables
    private int[] coloring;
    private boolean[][] domains;

    BacktrackingMAC(Graph graph, int k){
        coloring = new int[graph.points.length];
        domains = new boolean[graph.points.length][k];
        this.graph = graph;
        this.k = k;

        //Initial color state
        for(int i = 0; i < graph.points.length; i++){
            coloring[i] = -1;
        }
        //Initial domain values
        for(int i = 0; i < graph.points.length; i++){
            for(int j=0; j < k; j++){
                domains[i][j] = true;
            }
        }

        success = colorGraph(0);

        if(success){
            System.out.println("\n\tBacktrackingMAC: Successfully found solution with " + k + " colors.");
        }else{
            System.out.println("\n\tBacktrackingMAC: Failed to find solution with " + k + " colors.");
        }
    }

    private boolean colorGraph(int vertex){
        //Terminating case
        if(isSolved(vertex)) {
            if(Main.verbose) {
                System.out.println("\t\tBacktrackingMAC: Reached base case. Solution found.");
            }
            return true;
        }
        // For each value in order domain values
        for(int color = 0; color < k; color++) {
            if(Main.verbose) {
                System.out.println("\t\tBacktrackingMAC: Attempting to color vertex " + vertex + ".");
            }
            //Only color if it's in the domain
            if (canColor(vertex, color)) {
                //Set color
                coloring[vertex] = color;

                // Track iterations
                iterations++;
                if(iterations >= 40000) {
                    if(Main.verbose) {
                        System.out.println("\t\tBacktrackingMAC: Too many iterations. Giving up.");
                    }
                    return false;
                }
                //Set domain
                for (int x = 0; x < k; x++) {
                    if (x != color)
                        domains[vertex][x] = false;
                }
                // Update arc consistency
                if (!makeArcConsistent()) {
                    coloring[vertex] = -1;
                    //Reset domain
                    coloring[vertex] = -1;
                    restoreDomains();
                } else {
                    // Go deeper in search
                    if(colorGraph(vertex + 1))
                        return true;

                    // Last level didn't work, let's get ready for a new color
                    if(Main.verbose) {
                        System.out.println("\t\tBacktrackingMAC: Couldn't color below P" + vertex + ". Trying another color.");
                    }
                    coloring[vertex] = -1;
                    restoreDomains();
                }
            }
            if(Main.verbose) {
                System.out.println("\t\tBacktrackingMAC: Cannot color P" + vertex + " with color " + color + ".");
            }
        }
        if(Main.verbose) {
            System.out.println("\t\tBacktrackingMAC: Could not color P" + vertex + ". Backtracking.");
        }
        return false;
    }
    //Attempts to restore the domains when we backtrack a color
    private void restoreDomains(){
        //Initial domain values
        for(int i = 0; i < graph.points.length; i++){
            for(int j=0; j < k; j++){
                domains[i][j] = true;
            }
        }

        // Set domains for colors already set
        for(int i = 0; i < coloring.length; i++){
            if(coloring[i] > -1){
                for(int x = 0; x < domains[i].length; x++){
                    if (x != coloring[i])
                        domains[i][x] = false;
                }
            }
        }
        makeArcConsistent();

    }
    private boolean canColor(int point, int color) {
        //Check if this color has been ruled out
        if(!domains[point][color])
            return false;
        // Check each point connected to the point in question. If we've colored it the color "color" already, then
        // return false.
        for(Point checkPoint : graph.points[point].connectedPoints) {
            if(coloring[checkPoint.id] == color) {
                return false;
            }
        }
        return true;
    }

    // Check to see if the domain table has a solved path
    private boolean isSolved(int vertex) {
        if(vertex == graph.points.length)
            return true;
        // Just look through each domain and see if each row has 1 possible domain
        for(boolean[] domain: domains){
            int domainCount = 0;
            for(boolean aDomain : domain){
                if(aDomain)
                    domainCount++;
            }
            if (domainCount > 1 || domainCount == 0)
                return false;
        }
        return true;
    }

    // Implments AC3 algorithm
    private boolean makeArcConsistent(){
        // Temp domains, don't change real ones unless path is good
//        boolean[][] tempDomains = copyDomains(domains);
        //Examine each arc
        LinkedList<Integer> arcQueue = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<>();
        for(int i = 0; i < coloring.length; i++)
        {
            if(coloring[i] > -1)
                arcQueue.push(i);
        }

        while(!arcQueue.isEmpty()){
            int point = arcQueue.pop();
            if(!visited.contains(point)) {
                visited.add(point);
                for (Point connectedPoint : graph.points[point].connectedPoints) {
                    if (removeInconsistentValues(point, connectedPoint.id)) {
                        boolean isEmpty = true;
                        for (int x = 0; x < domains[connectedPoint.id].length; x++) {
                            if (domains[connectedPoint.id][x])
                                isEmpty = false;
                        }
                        // Bad path
                        if (isEmpty) {
//                            domains = tempDomains;
                            return false;
                        }

                        for (Point neighbors : graph.points[connectedPoint.id].connectedPoints) {
                            if (!visited.contains(neighbors.id))
                                arcQueue.push(neighbors.id);
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean removeInconsistentValues(int p1, int p2){
        boolean removed = false;
        for(int x = 0; x < domains[p2].length; x++){
            if(domains[p1][x] && !checkConstraints(x, p1)){
                domains[p2][x] = false;
                removed = true;
            }
        }

        return removed;
    }

    private boolean checkConstraints(int x, int p) {
        boolean isLegal = false;
        // Look for any value in p domain that is not equal to x
        for(int y = 0; y < domains[p].length; y++){
            // Check that y exists in p and y != x
            if(domains[p][y] && y != x)
                isLegal = true;
        }
        return isLegal;
    }

//    private boolean[][] copyDomains(boolean[][] domains){
//        boolean[][] copy = new boolean[domains.length][domains[0].length];
//        for(int i = 0; i < domains.length; i++){
//            System.arraycopy(domains[i], 0, copy[i], 0, domains[i].length);
//        }
//        return copy;
//    }

}
