package TSPAstar;

import java.util.List;

public class Vertex {
    private double g_val;
    private double h_val;
    private double f_val;
    private int cityId;
    private double[][] map;
    private List<Integer> state;
    // The constructor.
    public Vertex(double g_val, int cityId, double[][] map, List<Integer> state) {
        this.g_val = g_val;
        this.cityId = cityId;
        this.map = map;
        this.state = state;
    }
    // Getters and setters.
    public List<Integer> getState() {
        return state;
    }
    public double getG_val() {
        return g_val;
    }
    public void setH_val(double h_val) {
        this.h_val = h_val;
        setF_val(this.g_val + this.h_val);
    }
    public double getF_val() {
        return f_val;
    }
    public void setF_val(double f_val) {
        this.f_val = f_val;
    }
    public int getCityId() {
        return cityId;
    }
    public double[][] getMap() {
        return map;
    }
}