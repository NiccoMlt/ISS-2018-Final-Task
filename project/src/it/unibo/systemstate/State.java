package it.unibo.systemstate;

public class State {

    private String name;
    private Action[] actions;

    public State() {
        super();
        this.name = null;
        this.actions = null;
    }

    public State(String name, Action[] actions) {
        super();
        this.name = name;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Action[] getActions() {
        return actions;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }

}
