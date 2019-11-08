package it.unibo.systemstate;

public class SystemState {

    private WorldState world;
    private RobotState robot;
    private State state;

    public SystemState() {
        super();
        this.world = null;
        this.robot = null;
        this.state = null;
    }

    public SystemState(WorldState world, RobotState robot, State state) {
        super();
        this.world = world;
        this.robot = robot;
        this.state = state;
    }

    public WorldState getWorld() {
        return world;
    }

    public void setWorld(WorldState world) {
        this.world = world;
    }

    public RobotState getRobot() {
        return robot;
    }

    public void setRobot(RobotState robot) {
        this.robot = robot;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}