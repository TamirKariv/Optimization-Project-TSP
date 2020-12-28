package TSPGenetic;
// This class solves the problem.
import java.util.*;
// This class will perform our evolution.
public class traveling_salesman_problem {
    // Number of genomes in each generation.
    // Also called, population size.
    private int generation_size;
    // Length of genome.
    // Number of cities - 1.
    private int genome_size;
    // The number of cities.
    private int number_of_cities;
    // Number of genomes used to create next generation.
    // Also called, crossover rate.
    private int reproduction_size;
    // Maximum number of generations to create.
    private int max_iter;
    // Frequency of mutations when creating new generations.
    private float mutation_rate;
    // Size of tournament in tournament selection mode.
    private int tournament_size;
    // The type of selection, roulette or tournament.
    private selection_type type;
    // The distance matrix.
    private int[][] distance_matrix;
    // The starting city.
    private int starting_city;
    // Threshold of stopping, what we want to reach.
    private int fitness_threshold;
    // The constructor of the class.
    public traveling_salesman_problem(int number_of_cities, selection_type type, int[][] distance_matrix, int starting_city, int fitness_threshold) {
        this.number_of_cities = number_of_cities;
        this.genome_size = number_of_cities -1;
        this.type = type;
        this.distance_matrix = distance_matrix;
        this.starting_city = starting_city;
        this.fitness_threshold = fitness_threshold;
        // Set the parameters, can change freely.
        generation_size = 5000;
        reproduction_size = 200;
        max_iter = 1000;
        mutation_rate = 0.1f;
        tournament_size = 40;
    }
    // Create the initial population.
    public List<genome> initialPopulation() {
        List<genome> population = new ArrayList<>();
        for(int i = 0; i < generation_size; i++) {
            population.add(new genome(number_of_cities, distance_matrix, starting_city));
        }
        return population;
    }
    // Select reproduction size based on the selection method we chose earlier.
    public List<genome> selection(List<genome> population) {
        List<genome> selected = new ArrayList<>();
        for(int i = 0; i < reproduction_size; i++){
            if(type == selection_type.ROULETTE){
                selected.add(rouletteSelection(population));
            }
            else if(type == selection_type.TOURNAMENT){
                selected.add(tournamentSelection(population));
            }
        }
        return selected;
    }
    // Roulette selection.
    public genome rouletteSelection(List<genome> population) {
        int totalFitness = population.stream().map(genome::getFitness).mapToInt(Integer::intValue).sum();
        // Pick random point on our roulette.
        Random random = new Random();
        int selectedValue = random.nextInt(totalFitness);
        // Minimization, so we use a reciprocal value so the probability of selecting the genome
        // would be inversely propositional to its fitness.
        // Smaller fitness higher probability.
        float recValue = (float) 1/selectedValue;
        // Add the values and pick the genome that passed the threshold.
        float currentSum = 0;
        for(genome genome : population){
            currentSum += (float) 1/genome.getFitness();
            if(currentSum>=recValue){
                return genome;
            }
        }
        // If the above code didn't return, pick a random one to return.
        int selectRandom = random.nextInt(generation_size);
        return population.get(selectRandom);
    }
    // Picks n random elements from the population to enter tournament.
    public static <E> List<E> pickNRandomElements(List<E> list, int n) {
        Random r = new Random();
        int length = list.size();
        if (length < n) return null;
        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i , r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }
    // Tournament selection.
    // Best genome wins.
    public genome tournamentSelection(List<genome> population) {
        List<genome> selected = pickNRandomElements(population, tournament_size);
        return Collections.min(selected);
    }
    // If we pass a probability swap two cities in a genome.
    // If not, just return it.
    public genome mutate(genome salesman){
        Random random = new Random();
        float mutate = random.nextFloat();
        if(mutate < mutation_rate) {
            List<Integer> genome = salesman.getGenome();
            Collections.swap(genome, random.nextInt(genome_size), random.nextInt(genome_size));
            return new genome(genome, number_of_cities, distance_matrix, starting_city);
        }
        return salesman;
    }
    // Create new generation of children.
    public List<genome> createGeneration(List<genome> population) {
        List<genome> generation = new ArrayList<>();
        int currentGenerationSize = 0;
        while(currentGenerationSize < generation_size) {
            List<genome> parents = pickNRandomElements(population,2);
            List<genome> children = crossover(parents);
            children.set(0, mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));
            generation.addAll(children);
            currentGenerationSize+=2;
        }
        return generation;
    }
    // Atypical crossover because each genome is a list of cities.
    // Using partially mapped crossover.
    // Randomly pick a crossover point and swap elements between them.
    public List<genome> crossover(List<genome> parents) {
        // Setting up.
        Random random = new Random();
        int breakpoint = random.nextInt(genome_size);
        List<genome> children = new ArrayList<>();
        // Copy the parent genomes so we don't modify in case they were chosen in the crossover more than once.
        List<Integer> parent1Genome = new ArrayList<>(parents.get(0).getGenome());
        List<Integer> parent2Genome = new ArrayList<>(parents.get(1).getGenome());
        // Create first child.
        for(int i = 0; i < breakpoint; i++) {
            int newVal;
            newVal = parent2Genome.get(i);
            Collections.swap(parent1Genome,parent1Genome.indexOf(newVal),i);
        }
        children.add(new genome(parent1Genome, number_of_cities, distance_matrix, starting_city));
        // Reset parent.
        parent1Genome = parents.get(0).getGenome();
        // Create second child.
        for(int i = breakpoint; i < genome_size; i++) {
            int newVal = parent1Genome.get(i);
            Collections.swap(parent2Genome,parent2Genome.indexOf(newVal),i);
        }
        children.add(new genome(parent2Genome, number_of_cities, distance_matrix, starting_city));
        return children;
    }
    // Terminate if we reached max iterations or best genome shorter than target path length.
    public genome optimize() {
        List<genome> population = initialPopulation();
        genome globalBestGenome = population.get(0);
        for(int i = 0; i< max_iter; i++) {
            List<genome> selected = selection(population);
            population = createGeneration(selected);
            globalBestGenome = Collections.min(population);
            if(globalBestGenome.getFitness() < fitness_threshold)
                break;
        }
        return globalBestGenome;
    }
}