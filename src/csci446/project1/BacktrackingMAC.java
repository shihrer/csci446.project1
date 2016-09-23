package csci446.project1;

import csci446.project1.GraphSystem.Graph;
import csci446.project1.GraphSystem.Point;

import java.util.*;

public class BacktrackingMAC {
    private Graph graph;
    private int k;
    boolean success = false;
    int iterations = 0;
    // Holds a stack so it's easy to backtrack
//    int[] coloring;
//    HashSet<Integer>[] domains;

    BacktrackingMAC(Graph graph, int k){
        //Setup stacks
        int[] coloring = new int[graph.points.length];
        HashSet<Integer>[] domains = new HashSet[graph.points.length];

        this.graph = graph;
        this.k = k;

        //Initial color state
        for(int i = 0; i < graph.points.length; i++){
            coloring[i] = -1;
        }
        //Initial domain values
        for(int i = 0; i < graph.points.length; i++){
            domains[i] = new HashSet<Integer>();
            for(int j=0; j < k; j++){
                domains[i].add(j);
            }
        }

        success = colorGraph(0, coloring.clone(), domains.clone());

        if(success){
            System.out.println("\tBacktracking with MAC: Successfully found solution with " + k + " colors.");
        }else{
            System.out.println("\tBacktracking with MAC: Failed to find solution with " + k + " colors.");
        }
    }

    private boolean colorGraph(int vertex, int[] coloring, HashSet<Integer>[] domains){
        if(vertex == graph.points.length)
            return true;

        for(int color = 0; color < k; color++)
        {
            if(canColor(vertex, color, coloring)) {
                coloring[vertex] = color;
                // Update arc consistency
                makeArcConsistent(vertex, domains);
                if(isEmptyDomains(domains)){
                    // Unsolvable, backtrack
                    return false;
                }

                iterations++;
                if (colorGraph(vertex + 1, coloring.clone(), domains.clone())) {
                    return true;
                }

                coloring[vertex] = -1;
            }
        }
        return false;
    }

    private boolean isEmptyDomains(HashSet[] domains) {
        for(HashSet domain : domains){
            if(domain.isEmpty())
                return true;
        }
        return false;
    }

    private boolean canColor(int vertex, int color, int[] coloring){
        // Check each point connected to the point in question. If we've colored it the color "color" already, then
        // return false.  Check the cspDomains to see if the color is a valid choice (forward checking).  Check the
        // constraints as well.  If there are any adjacent nodes who only have one choice left.
        for(Point checkPoint : graph.points[vertex].connectedPoints) {
            if(coloring[checkPoint.id] == color) {
                return false;
            }
        }
        return true;
    }

    private void makeArcConsistent(int vertex, HashSet<Integer>[] domains){
        //Examine each arc
        LinkedList<Integer> arcQueue = new LinkedList<>();
        arcQueue.push(vertex);
        while(!arcQueue.isEmpty()){
            int point = arcQueue.pop();
            for(Point connectedPoint : graph.points[point].connectedPoints){
                if(removeInconsistentValues(point, connectedPoint.id, domains)){
                    for(Point neighbors : graph.points[connectedPoint.id].connectedPoints){
                        arcQueue.push(neighbors.id);
                    }
                }

            }
        }
    }

    private boolean removeInconsistentValues(int p1, int p2, HashSet<Integer>[] domains){
        boolean removed = false;
        for(int x : domains[p2]){
            if(!checkConstraints(x, p1, domains)){
                domains[p2].remove(x);
                removed = true;
            }
        }

        return removed;
    }

    private boolean checkConstraints(int x, int p, HashSet<Integer>[] domains) {
        boolean isLegal = false;
        // If only 1 value,
        for(int y : domains[p]) {
            if(y != x)
                isLegal = true;
        }
        return isLegal;
    }

}
