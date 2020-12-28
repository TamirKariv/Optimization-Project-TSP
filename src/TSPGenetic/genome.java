package TSPGenetic;
// This class represents the solution genome.
import java.util.*;
// Implement comparable to make it easier to compare and calculate fitness.
public class genome implements Comparable {
    // The final path.
    List<Integer> genome;
    // The prices of traveling between cities.
    int[][] distance_matrix;
    // The starting city, should be the same between genomes.
    int starting_city;
    // The amount of cities visited.
    int number_of_cities = 0;
    // The fitness.
    int fitness;
    // Getters and setters for the class.
    // Get the genome.
    public List<Integer> getGenome() {
        return genome;
    }
    // Get the starting city.
    public int getStarting_city() {
        return starting_city;
    }
    // Get the fitness.
    public int getFitness() {
        return fitness;
    }
    // Set the fitness.
    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
    // Constructor creates a random genome.
    public genome(int number_of_cities, int[][] distance_matrix, int starting_city){
        this.distance_matrix = distance_matrix;
        this.starting_city = starting_city;
        this.number_of_cities = number_of_cities;
        genome = randomSalesman();
        fitness = this.calculateFitness();
    }
    // Constructor creates a user defined genome.
    public genome(List<Integer> permutation_of_cities, int number_of_cities, int[][] distance_matrix, int starting_city){
        genome = permutation_of_cities;
        this.distance_matrix = distance_matrix;
        this.starting_city = starting_city;
        this.number_of_cities = number_of_cities;
        fitness = this.calculateFitness();
    }
    // Finds the fitness.
    // Adds up the cost of the genome path using the distance matrix.
    // Finds the actual cost of the path, what we want to minimize.
    public int calculateFitness(){
        int fitness = 0;
        int currentCity = starting_city;
        // Sums the costs.
        for (int gene : genome) {
            fitness += distance_matrix[currentCity][gene];
            currentCity = gene;
        }
        // Add the starting city, starts at index 0.
        fitness += distance_matrix[genome.get(number_of_cities -2)][starting_city];
        return fitness;
    }
    // Generate the random genome.
    // Genome is are permutations of the list of cities without the starting city.
    private List<Integer> randomSalesman(){
        List<Integer> result = new ArrayList<Integer>();
        for(int i = 0; i< number_of_cities; i++) {
            if(i != starting_city) {
                result.add(i);
            }
        }
        Collections.shuffle(result);
        return result;
    }
    // Override the toString method to print results.
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Minimal cost of the path: ");
        builder.append(this.fitness);
        builder.append("\nThe path: ");
        builder.append(starting_city);
        for (int gene: genome) {
            builder.append(" ");
            builder.append(gene);
        }
        builder.append(" ");
        builder.append(starting_city);
        return builder.toString();
    }
    // Override the compareTo result to change the way we compare objects of this type.
    @Override
    public int compareTo(Object o) {
        genome genome = (genome) o;
        if(this.fitness > genome.getFitness()) {
            return 1;
        }
        else if(this.fitness < genome.getFitness()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}