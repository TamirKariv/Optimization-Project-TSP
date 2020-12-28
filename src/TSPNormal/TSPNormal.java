package TSPNormal;// COMPLETE.
// Version 1: Solve traveling salesman using the branch and bound method.
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
// The SolveProblem class contains useful information.
public class TSPNormal {
    // The number of cities.
    private int number_of_cities;
    // The nodes already visited in the current path.
    private boolean visited_nodes[];
    // The final minimum weight of the shortest path..
    private int minimum_cost = Integer.MAX_VALUE;
    // The final path of the salesman (Solution).
    private int results_path[];
    // The estimated time.
    private double estimated_time;
    // Gets the time.
    public double get_time() {
        return this.estimated_time;
    }
    // Gets the cost.
    public int get_cost() {
        return this.minimum_cost;
    }
    // Gets the number of cities in the file (size of matrix n x n ).
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
    // Generates the distance matrix from the file.
    public int[][] gen_matrix(String source) {
        // Get number of cities to know size of matrix.
        int cityCount = get_num_of_cities(source);
        // If there is no city
        if (cityCount < 1) {
            return null;
        }
        // Read the file and create the matrix.
        Scanner scanner;
        int[][] distances = new int[cityCount][cityCount];
        try {
            // Input the file path.
            scanner = new Scanner(new File(source));
            // Cost Matrix.
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
    // Copy temp solution to the final one.
    public void copy_final_solution(int current_path[]) {
        for (int i = 0; i < number_of_cities; i++) {
            results_path[i] = current_path[i];
        }
        results_path[number_of_cities] = current_path[0];
    }
    // Find minimum edge cost ending at v.
    public int find_first_min(int values[][], int v) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < number_of_cities; i++) {
            if (values[v][i] < min && v != i) {
                min = values[v][i];
            }
        }
        return min;
    }
    // Find second minimum edge cost ending at v.
    public int find_second_min(int values[][], int v) {
        int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE;
        for (int i = 0; i < number_of_cities; i++) {
            if (v == i) {
                continue;
            }
            if (values[v][i] <= first) {
                second = first;
                first = values[v][i];
            } else if (values[v][i] <= second && values[v][i] != first) {
                second = values[v][i];
            }
        }
        return second;
    }
    // Solves the problem recursively.
    // values is our distance matrix.
    // current bound is the lower bound of the root node.
    // current weight stores the weight of the current path.
    // level is the current depth of the search.
    // curr_path[] stores the solution to later copy to the final solution.
    public void solve(int[][] values, int current_bound, int current_weight, int level, int current_path[]) {
        // If we reached the last level, covered all the nodes.
        if (level == number_of_cities) {
            // Check if there is an edge from last node to first node.
            if (values[current_path[level - 1]][current_path[0]] != 0) {
                // Total weight of solution.
                int curr_res = current_weight +
                        values[current_path[level - 1]][current_path[0]];
                // Change final result if current one is better.
                if (curr_res < minimum_cost) {
                    copy_final_solution(current_path);
                    minimum_cost = curr_res;
                }
            }
            return;
        }
        // Builds the search tree recursively.
        for (int i = 0; i < number_of_cities; i++) {
            // Visit next node.
            if (values[current_path[level - 1]][i] != 0 && !visited_nodes[i]) {
                int temp = current_bound;
                current_weight += values[current_path[level - 1]][i];
                // Different computation of curr_bound for level 2 from the other levels.
                if (level == 1) {
                    current_bound -= ((find_first_min(values, current_path[level - 1]) + find_first_min(values, i)) / 2);
                } else {
                    current_bound -= ((find_second_min(values, current_path[level - 1]) + find_first_min(values, i)) / 2);
                }
                // current_bound + current_weight is the real lower bound of the node that we reached.
                // If current lower bound < minimum cost we need to explore deeper.
                if (current_bound + current_weight < minimum_cost) {
                    current_path[level] = i;
                    visited_nodes[i] = true;
                    // call TSPRec for the next level.
                    solve(values, current_bound, current_weight, level + 1,
                            current_path);
                }
                // Prune node by resetting all changes to current weight gand current bound.
                current_weight -= values[current_path[level - 1]][i];
                current_bound = temp;
                // Reset the visited nodes.
                Arrays.fill(visited_nodes, false);
                for (int j = 0; j <= level - 1; j++)
                    visited_nodes[current_path[j]] = true;
            }
        }
    }
    // Sets up the problem and calls the solver.
    public void traveling_salesman_problem(int[][] values) {
        int[] current_path = new int[number_of_cities + 1];
        // Calculate initial lower bound for the root node and initialize the current path and visited array.
        int current_bound = 0;
        Arrays.fill(current_path, -1);
        Arrays.fill(visited_nodes, false);
        // Find initial bound
        for (int i = 0; i < number_of_cities; i++) {
            current_bound += (find_first_min(values, i) + find_second_min(values, i));
        }
        // Round lower bound to int.
        current_bound = (current_bound == 1) ? current_bound / 2 + 1 : current_bound / 2;
        // We start with vertex 1, so the first vertex in current_path[] is 0
        visited_nodes[0] = true;
        current_path[0] = 0;
        // Solve with calculated value.s
        solve(values, current_bound, 0, 1, current_path);
    }
    // TSPNormal function.
    public void SolveProblem(String problem) {
        // Get the distance matrix.
        int[][] values = gen_matrix("src/" + problem);
        number_of_cities = get_num_of_cities("src/" + problem);
        visited_nodes = new boolean[number_of_cities];
        results_path = new int[number_of_cities + 1];
        // Count the amount of time it took to solve this problem.
        // Start timer.
        long start = System.nanoTime();
        // Solve problem.
        traveling_salesman_problem(values);
        // End timer.
        long elapsedTime = System.nanoTime() - start;
        // Print results.
        System.out.println("");
        System.out.println("Normal TSP Results: ");
        System.out.printf("The minimum cost of the path: %d\n", minimum_cost);
        System.out.printf("The path taken: ");
        System.out.println(Arrays.toString(results_path));
        this.estimated_time = (double)elapsedTime/1000000000;
        System.out.printf("The time it took to solve in seconds: %f\n", this.estimated_time);


    }
}

