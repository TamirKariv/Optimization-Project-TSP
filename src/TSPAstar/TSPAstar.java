package TSPAstar;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
// Create the A* traveling salesman solver class.
public class TSPAstar {
    public int num_of_explored_nodes = 0;
    public double estimated_cost;
    public double estimated_time;
    public double cost;
    // Get the cost.
    public double get_cost() {
        return this.cost;
    }
    // Solve the problem.
    public void SolveProblem(String problem) {
        //source file for graph.
        String source =  "src/" + problem;
        //the number of cities for the algorithm.
        int number_of_cities = get_num_of_cities(source);
        //solve the tsp problem using the A* algorithm.
        long start = System.nanoTime();
        solve(number_of_cities, source);
        long elapsedTime = System.nanoTime() - start;
        this.estimated_time = (double)elapsedTime/1000000000;
        System.out.printf("The time it took to solve in seconds: %f\n", this.estimated_time);
    }
    // Get the estimated time.
    public double get_time() {
        return this.estimated_time;
    }
    // Get the number of the cities from the file.
    public static int get_num_of_cities(String path) {
        int numOfCities = 0;
        String line;
        //get the first line of the matrix from the file to determine it's size
        try (
                InputStream fis = new FileInputStream(path);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr)) {
                line = br.readLine();
                String[] str_arr = line.split(" ");
                numOfCities = str_arr.length;
        }
        //handle io error if file not found.
        catch (IOException e) {
            e.printStackTrace();
        }
        // return the number of cities.
        return numOfCities;
    }
    //create the tsp problem and use astar to solve it.
    private void solve(int cityCount, String source) {
        double[][] distances = gen_matrix(source);
        //initialize the states
        List<Integer> states = new ArrayList<>();
        states.add(0);
        //create the root vertex
        Vertex rootVertex = new Vertex(0.0, 0, distances, states);
        //frontier to store the vertices and their f costs
        PriorityQueue<Vertex> frontier = new PriorityQueue<>((a, b) -> {
            if (a.getF_val() > b.getF_val()) {
                return 1;
            }
            else if (a.getF_val() < b.getF_val()) {
                return -1;
            }
            else return 0;
        });
        //maps for quick retrieval of vertices
        Map<List<Integer>, Vertex> explored = new HashMap<>();
        Map<List<Integer>, Vertex> frontierMap = new HashMap<>();
        this.estimated_cost = HeuristicCalculation(rootVertex, cityCount);
        rootVertex.setH_val(this.estimated_cost);
        frontier.offer(rootVertex);
        frontierMap.put(rootVertex.getState(), rootVertex);
        //get the end vertex reconstruct the path from it's states.
        Vertex endVertex = find_path(explored, frontier, frontierMap, cityCount);
        System.out.println("");
        System.out.println("Astar Reults: ");
        System.out.print("Travelling Salesman Path = ");
        List<Integer> path = new ArrayList<>();
        //print the path
        if (endVertex != null) {
            for (Integer i : endVertex.getState()) {
                path.add(i);
                System.out.print(i + 1 + " ");
            }
            System.out.println("");
        }
        //print the results
        System.out.println("Number of Nodes expanded = " + this.num_of_explored_nodes);
        System.out.println("Estimated cost by heuristic function: " + this.estimated_cost);
        this.cost = endVertex.getG_val();
        System.out.println("Actual cost: " + endVertex.getG_val());
    }
    //generate the problem's graph from the file.
    private double[][] gen_matrix(String source){
        int cityCount = get_num_of_cities(source);
        Scanner scanner;
        double[][] distances = new double[cityCount][cityCount];
        try {
            //Input the file path
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
    //Calculates the Heuristic
    public Double HeuristicCalculation(Vertex vertex, int cityCount) {
        return MSTCalculation(vertex, cityCount) + MinCostToRoot(vertex, cityCount)
                + MinCostToSuccessor(vertex, cityCount);
    }
    // Calculate the min cost to successor
    public Double MinCostToSuccessor(Vertex vertex, int cityCount) {
        //check if all the cities were visited
        if (vertex.getState().size() >= cityCount) {
            return 0.0;
        }
        double minToSuccessor = Double.MAX_VALUE;
        for (int i = 0; i < cityCount; i++) {
            //finding the minimum cost
            if (!vertex.getState().contains(i) && vertex.getMap()[vertex.getCityId()][i] < minToSuccessor) {
                minToSuccessor = vertex.getMap()[vertex.getCityId()][i];
            }
        }
        return minToSuccessor;
    }
    //Calculate the min cost to the root vertex
    public Double MinCostToRoot(Vertex vertex, int cityCount) {
        //check if all the cities were visited
        if (vertex.getState().size() >= cityCount) {
            return vertex.getMap()[vertex.getCityId()][0];
        }
        double minToRoot = Double.MAX_VALUE;
        //finding the minimum cost
        for (int i = 0; i < cityCount; i++) {
            if (!vertex.getState().contains(i) && vertex.getMap()[i][0] < minToRoot) {
                minToRoot = vertex.getMap()[i][0];
            }
        }
        return minToRoot;
    }
    //Calculate the sum of edges in a MST from the distance matrix
    public double MSTCalculation(Vertex vertex, int cityCount) {
        //if only single vertex remains
        if (cityCount - vertex.getState().size() < 2) {
            return 0.0;
        }
        double cost = 0.0;
        List<Integer> visitedCities = new ArrayList<>();
        int minCity1 = -1;
        int minCity2 = -1;
        double minDist = Double.MAX_VALUE;
        //Find the first minimum edge of the MST
        for (int i = 0; i < cityCount; i++) {
            for (int j = 0; j < cityCount; j++) {
                if (!vertex.getState().contains(i) && !vertex.getState().contains(j) && i != j) {
                    if (vertex.getMap()[i][j] < minDist) {
                        minDist = vertex.getMap()[i][j];
                        minCity1 = i;
                        minCity2 = j;
                    }
                }
            }
        }
        visitedCities.add(minCity1);
        visitedCities.add(minCity2);
        cost = cost + vertex.getMap()[minCity1][minCity2];
        //Find remaining edges
        while (visitedCities.size() < (cityCount - vertex.getState().size())) {
            int minC = -1;
            double minD = Double.MAX_VALUE;
            for (Integer visitedCity : visitedCities) {
                for (int i = 0; i < cityCount; i++) {
                    //skip the vertex if it's in the state's list
                    if (!vertex.getState().contains(i) && !visitedCities.contains(i)) {
                        if (vertex.getMap()[visitedCity][i] < minD) {
                            minD = vertex.getMap()[visitedCity][i];
                            minC = i;
                        }
                    }
                }
            }
            visitedCities.add(minC);
            cost = cost + minD;
        }
        return cost;
    }
    //find the travelling salesman path using the astar algorithm
    public Vertex find_path(Map<List<Integer>, Vertex> explored, PriorityQueue<Vertex> frontier, Map<List<Integer>, Vertex> frontierMap, int cityCount) {
        while (!frontier.isEmpty()) {
            Vertex current = frontier.poll();
            frontierMap.remove(current.getState());
            if (current.getState().size() == cityCount + 1 && current.getState().get(0) == 0 && current.getState().get(cityCount) == 0) {
                return current;
            } else {
                explored.put(current.getState(), current);
                this.num_of_explored_nodes++;
                List<Vertex> successors = getSuccessors(current, cityCount);
                for (Vertex successor : successors) {
                    if (!explored.containsKey(successor.getState()) && !frontierMap.containsKey(successor.getState())) {
                        frontier.offer(successor);
                        frontierMap.put(successor.getState(), successor);
                    } else if (frontierMap.containsKey(successor.getState()) && frontierMap.get(successor.getState()).getF_val() > successor.getF_val()) {
                        frontier.remove(frontierMap.get(successor.getState()));
                        frontier.add(successor);
                        frontierMap.put(successor.getState(), successor);
                    }
                }
            }
        }
        //if no path is found
        return null;
    }
    //get the successors
    public List<Vertex> getSuccessors(Vertex parent, Integer cityCount) {
        List<Vertex> successors = new ArrayList<>();
        for (int i = 0; i < cityCount; i++) {
            if (!parent.getState().contains(i)) {
                //add them in the same order
                List<Integer> successorState = new ArrayList<>(parent.getState());
                successorState.add(i);
                Vertex successor = new Vertex(parent.getG_val() + parent.getMap()[parent.getCityId()][i], i, parent.getMap(), successorState);
                successor.setH_val(HeuristicCalculation(successor, cityCount));
                successors.add(successor);
            }
        }
        //If no successors were added
        if (successors.size() == 0) {
            List<Integer> goalState = new ArrayList<>(parent.getState());
            goalState.add(0);
            Vertex successor = new Vertex(parent.getG_val() + parent.getMap()[parent.getCityId()][0],  0, parent.getMap(), goalState);
            successor.setH_val(HeuristicCalculation(successor, cityCount));
            successors.add(successor);
        }
        return successors;
    }
}