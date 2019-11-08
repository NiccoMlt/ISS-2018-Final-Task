package it.unibo.systemstate;

public class WorldState {

    private String temperature;
    private String[][] map;

    public WorldState() {
        super();
        this.temperature = null;
        this.map = null;
    }

    public WorldState(String temperature, String[][] map) {
        super();
        this.temperature = temperature;
        this.map = map;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String[][] getMap() {
        return map;
    }

    public void setMap(String[][] map) {
        this.map = map;
    }
}