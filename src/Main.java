// Importing the algorithms from the classes.
import TSPAstar.*;
import TSPGenetic.*;
import TSPNormal.*;

import java.util.Arrays;

public class Main {
    // The main method.
    public static void main(String[] args) {
        // The times for each kind of problem.
        double[] cost_of_nodes = new double[3];
        double[] directed_or_not = new double[3];
        double[] equal_costs = new double[3];
        double[] number_of_nodes = new double[3];
        int index = 0;
        // The problems in the form of text files.
        String [] problems = {"cost_of_nodes/max_3.txt","cost_of_nodes/max_200.txt","cost_of_nodes/max_900.txt",
                "directed_or_not/10.txt","directed_or_not/15.txt","directed_or_not/20.txt",
                "equal_costs/10.txt","equal_costs/15.txt","equal_costs/30.txt",
                    "number_of_nodes/15.txt", "number_of_nodes/20.txt","number_of_nodes/30.txt","number_of_nodes/40.txt",
                "number_of_nodes/50.txt"};
        // Iterate the problems.
        for (String problem : problems) {
            // Print the problem.
            System.out.println("The Problem we solved: " +  problem);
            //Normal TSP
            TSPNormal tspNormal = new TSPNormal();
            tspNormal.SolveProblem(problem);
            index++;
            //A* TSP
            TSPAstar tspAstar = new TSPAstar();
            tspAstar.SolveProblem(problem);
            index++;
            //Genetic TSP
            TSPGenetic tspGenetic = new TSPGenetic();
            tspGenetic.SolveProblem(problem);
            index++;
            System.out.println("");
            // Print the times of every set of 3 problems.
            // (Change indexes when adding or removing problems). *************
            if (index <= 9) {
                cost_of_nodes[0] = tspNormal.get_time();
                cost_of_nodes[1] = tspAstar.get_time();
                cost_of_nodes[2] = tspGenetic.get_time();
                System.out.println("The times are: ");
                System.out.println(Arrays.toString(cost_of_nodes));
                System.out.println("The costs are: ");
                System.out.println("Normal");
                System.out.println(tspNormal.get_cost());
                System.out.println("Astar");
                System.out.println(tspAstar.get_cost());
                System.out.println("Genetic");
                System.out.println(tspGenetic.get_cost());
            }
            if (9 < index && index <= 18) {
                directed_or_not[0] = tspNormal.get_time();
                directed_or_not[1] = tspAstar.get_time();
                directed_or_not[2] = tspGenetic.get_time();
                System.out.println("The times are: ");
                System.out.println(Arrays.toString(directed_or_not));
                System.out.println("The costs are: ");
                System.out.println("Normal");
                System.out.println(tspNormal.get_cost());
                System.out.println("Astar");
                System.out.println(tspAstar.get_cost());
                System.out.println("Genetic");
                System.out.println(tspGenetic.get_cost());
            }
            if (18 < index && index <= 27) {
                equal_costs[0] = tspNormal.get_time();
                equal_costs[1] = tspAstar.get_time();
                equal_costs[2] = tspGenetic.get_time();
                System.out.println("The times are: ");
                System.out.println(Arrays.toString(equal_costs));
                System.out.println("The costs are: ");
                System.out.println("Normal");
                System.out.println(tspNormal.get_cost());
                System.out.println("Astar");
                System.out.println(tspAstar.get_cost());
                System.out.println("Genetic");
                System.out.println(tspGenetic.get_cost());
            }
            if (27 < index && index <= 36) {
                number_of_nodes[0] = tspNormal.get_time();
                number_of_nodes[1] = tspAstar.get_time();
                number_of_nodes[2] = tspGenetic.get_time();
                System.out.println("The times are: ");
                System.out.println(Arrays.toString(number_of_nodes));
                System.out.println("The costs are: ");
                System.out.println("Normal");
                System.out.println(tspNormal.get_cost());
                System.out.println("Astar");
                System.out.println(tspAstar.get_cost());
                System.out.println("Genetic");
                System.out.println(tspGenetic.get_cost());
            }
            if (36 < index && index <= 45) {
                number_of_nodes[0] = tspNormal.get_time();
                number_of_nodes[1] = tspAstar.get_time();
                number_of_nodes[2] = tspGenetic.get_time();
                System.out.println("The times are: ");
                System.out.println(Arrays.toString(number_of_nodes));
                System.out.println("The costs are: ");
                System.out.println("Normal");
                System.out.println(tspNormal.get_cost());
                System.out.println("Astar");
                System.out.println(tspAstar.get_cost());
                System.out.println("Genetic");
                System.out.println(tspGenetic.get_cost());
            }

        }
    }
}
