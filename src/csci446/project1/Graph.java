package csci446.project1;

/**
 * Created by cetho on 9/1/2016.
 */
public class Graph {

    private Point[] points;

    public Graph(int points) {
        //Create an empty array to store the points in the graph.
        this.points = new Point[points];

        float[] xValues = new float[points];
        float[] yValues = new float[points];

        for(int i = 0; i < points; i++) {
            boolean duplicate = true;
            while(duplicate) {
                xValues[i] = (float) Math.random();
                yValues[i] = (float) Math.random();

                for(int j = 0; j < points; j++ ) {
                    if(xValues[j] == 0.0f) {
                        //These values haven't been initialized yet.
                        duplicate = false;
                        break;
                    }
                    if(xValues[j] == xValues[i] && i != j) {
                        if(yValues[j] == yValues[i]) {
                            //There is a duplicate point. Recreate this point.
                            break;
                        }
                    }
                }
                duplicate = false;
            }
            //Officially create the point and store it in the object.
            this.points[i] = new Point(xValues[i], yValues[i]);
        }

        //Print out the points.
        System.out.println("Points Created:");
        for (Point point : this.points) {
            point.printPoint();
        }
    }

    public Point[] points() {
        return this.points;
    }

}
