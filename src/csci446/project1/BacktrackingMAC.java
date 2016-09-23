package csci446.project1;

import csci446.project1.GraphSystem.Graph;
import csci446.project1.GraphSystem.Point;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

public class BacktrackingMAC {
    private Graph graph;
    private int k;
    boolean success = false;
    int iterations = 0;
    // Holds a stack so it's easy to backtrack
    Stack<int[]> colorings;
    Stack<HashSet<Integer>[]> domains;

    BacktrackingMAC(Graph graph, int k){
        this.graph = graph;
        this.k = k;
        this.colorings.push(new int[k]);
        //Initial color state
        for(int i = 0; i < k; i++){
            colorings.peek()[i] = -1;
        }

        domains.push(new HashSet[graph.points.length]);
        //Initial domain values
        for(int i = 0; i < graph.points.length; i++){
            for(int j=0; j < k; j++){
                domains.peek()[i].add(j);
            }
        }

        success = colorGraph(0);

        if(success){
            System.out.println("\tBacktracking with MAC: Successfully found solution with " + k + " colors.");
        }else{
            System.out.println("\tBacktracking with MAC: Failed to find solution with " + k + " colors.");
        }
    }

    boolean colorGraph(int vertex){
        if(vertex == graph.points.length)
            return true;

        for(int color = 0; color < k; color++)
        {
            if(canColor(vertex, color)) {
                colorings.peek()[vertex] = color;
                // Update arc consistency
                makeArcConsistent(vertex);
                if(isEmptyDomains()){
                    // Unsolvable, backtrack
                    colorings.pop();
                    domains.pop();
                    return false;
                }

                iterations++;
                if (colorGraph(vertex + 1)) {
                    return true;
                }

                colorings.peek()[vertex] = -1;
            }
        }
        colorings.pop();
        domains.pop();
        return false;
    }

    private boolean isEmptyDomains() {
        for(HashSet domain : domains.peek()){
            if(domain.isEmpty())
                return true;
        }
        return false;
    }

    private boolean canColor(int vertex, int color){
        // Check each point connected to the point in question. If we've colored it the color "color" already, then
        // return false.  Check the cspDomains to see if the color is a valid choice (forward checking).  Check the
        // constraints as well.  If there are any adjacent nodes who only have one choice left.
        for(Point checkPoint : graph.points[vertex].connectedPoints) {
            if(colorings.peek()[checkPoint.id] == color) {
                return false;
            }
        }
        return true;
    }

    private void makeArcConsistent(int vertex){
        //Examine each arc
        LinkedList<Integer> arcQueue = new LinkedList<>();
        arcQueue.push(vertex);
        while(!arcQueue.isEmpty()){
            int point = arcQueue.pop();
            for(Point connectedPoint : graph.points[point].connectedPoints){
                if(removeInconsistentValues(point, connectedPoint.id)){
                    for(Point neighbors : graph.points[connectedPoint.id].connectedPoints){
                        arcQueue.push(neighbors.id);
                    }
                }

            }
        }
    }

    private boolean removeInconsistentValues(int p1, int p2){
        boolean removed = false;
        for(int x : domains.peek()[p2]){
            if(!checkConstraints(x, p1)){
                domains.peek()[p2].remove(x);
                removed = true;
            }
        }

        return removed;
    }

    private boolean checkConstraints(int x, int p) {
        boolean isLegal = false;
        // If only 1 value,
        for(int y : domains.peek()[p]) {
            if(y != x)
                isLegal = true;
        }
        return isLegal;
    }

}
