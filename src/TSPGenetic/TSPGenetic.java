package TSPGenetic;// COMPLETE.
// Version 3: using a Genetic algorithm to solve the traveling salesman problem.
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
// The SolveProblem class.
public class TSPGenetic {
    // The time member.
    public double estimated_time;
    // The cost.
    public double cost;
    // Get the estimated time.
    public double get_time() {
        return this.estimated_time;
    }
    // Get the cost.
    public double get_cost() {
        return this.cost;
    }
    // Gets the number of cities from the file.
    public int get_num_of_cities(String path) {
        int cityCount = 0;
        String line;
        try (
                InputStream fis = new FileInputStream(path);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr)) {
            line = br.readLine();
            String[] str_arr = line.split(" ");
            cityCount = str_arr.length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityCount;
    }
    // Generate the distance matrix from the file.
    public int[][] gen_matrix(String source) {
        int cityCount = get_num_of_cities(source);
        // If there are no cities.
        if (cityCount < 1) {
            return null;
        }
        Scanner scanner;
        int[][] distances = new int[cityCount][cityCount];
        try {
            // Input the file path.
            scanner = new Scanner(new File(source));
            //Cost Matrix
            for (int i = 0; i < cityCount; i++) {
                for (int j = 0; j < cityCount && scanner.hasNextInt(); j++) {
                    int nxtInt = scanner.nextInt();
                    distances[i][j] = nxtInt;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return distances;
    }
    // The SolveProblem method.
    public void SolveProblem(String problem) {
        // Get the number of cities.
        int numberOfCities = get_num_of_cities("src/" + problem);
        // Create the distance matrix.
        int[][] travelPrices = gen_matrix("src/" + problem);
        // Count the amount of time it took to solve this problem.
        // Start timer.
        long start = System.nanoTime();
        // Solve the problem.
        traveling_salesman_problem geneticAlgorithm = new traveling_salesman_problem(numberOfCities, selection_type.ROULETTE, travelPrices, 0, 0);
        genome result = geneticAlgorithm.optimize();
        // End timer.
        long elapsedTime = (System.nanoTime() - start);
        // Print results.
        System.out.println("");
        System.out.println("Genetic TSP Results: ");
        this.estimated_time =  (double)elapsedTime/1000000000;
        System.out.printf("The time it took to solve in seconds: %f\n", this.estimated_time);
        System.out.println(result);
        // Set cost.
        this.cost = result.getFitness();
    }
}