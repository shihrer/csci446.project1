package csci446.project1;

/**
 * Created by michael on 9/22/2016.
 */
public class Chromosome {
    private int[] chromosome;
    private int fitness;
    private int penaltyFactor;

    Chromosome(int[] chromosome){
        this.chromosome = chromosome;
        this.fitness = -1;
        this.penaltyFactor = 1;
    }

    public int getFitness(){
        // Return the fitness with a penalty factor divided by length of chromosome.
        // Shorter the chromosome, the more penalty it has for conflicts
        return fitness * penaltyFactor/chromosome.length;
    }
    public void setFitness(int fitness){
        this.fitness = fitness;
    }
    public int[] getChromosome(){
        return chromosome;
    }
    public void setChromosome(int[] chromosome){
        this.chromosome = chromosome;
    }

    public int getNodeColor(int id) {
        return chromosome[id];
    }
    public void increasePenalty(){
        penaltyFactor++;
    }
    @Override
    public String toString(){
        StringBuilder chromeString = new StringBuilder();
        for(int i = 0; i < chromosome.length; i++)
            chromeString.append(chromosome[i]);

        return chromeString.toString();
    }
}
