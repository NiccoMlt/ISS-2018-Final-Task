package it.unibo.systemstate;

public class RobotState {
    
    private Position position;
    private String direction;
    private String info;
    
    public RobotState() {
        super();
        this.position = null;
        this.direction = null;
        this.info = null;
    }

    public RobotState(Position position, String direction, String info) {
        super();
        this.position = position;
        this.direction = direction;
        this.info = info;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    
}