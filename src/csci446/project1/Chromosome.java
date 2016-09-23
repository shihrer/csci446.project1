package csci446.project1;

/**
 * Created by michael on 9/22/2016.
 */
public class Chromosome {
    private String chromosome;
    private int fitness;

    Chromosome(String chromosome){
        this.chromosome = chromosome;
        this.fitness = -1;
    }

    public int getFitness(){
        return fitness;
    }
    public void setFitness(int fitness){
        this.fitness = fitness;
    }

    public String getChromosome(){
        return chromosome;
    }

    public int getNodeColor(int id) {
        return chromosome.charAt(id);
    }
}
