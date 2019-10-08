package it.unibo.systemstate;

public class Action {

    private String name;
    private String type;
    private String cmd;

    public Action() {
        super();
        this.name = null;
        this.type = null;
        this.cmd = null;
    }

    public Action(String name, String type, String cmd) {
        super();
        this.name = name;
        this.type = type;
        this.cmd = cmd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

}