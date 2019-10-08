package it.unibo.systemstate;

import com.google.gson.Gson;

import it.unibo.qactors.akka.QActor;

public class systemStateUtil {

    private static systemStateUtil singletonStateUtil;
    private static SystemState systemState;

    private static Gson gson = new Gson();

    public static systemStateUtil getSystemStateUtil() {
        if (singletonStateUtil == null)
            singletonStateUtil = new systemStateUtil();
        return singletonStateUtil;
    }

    private systemStateUtil() {
        super();
        systemState = new SystemState(new WorldState(null, null), new RobotState(new Position(), null, null),
                new State());
    }

    public static void updateTemperature(QActor qa, String value) {
        systemState.getWorld().setTemperature(value);
        notifyUpdateState(qa);
    }

    public static void updateMap(QActor qa, String map) {
        systemState.getWorld().setMap(gson.fromJson(map, String[][].class));
        notifyUpdateState(qa);
    }

    public static void updateRobotMovement(QActor qa, String robotMovment) {
        systemState.setRobot(gson.fromJson(robotMovment, RobotState.class));
        notifyUpdateState(qa);
    }

    public static void updateRobotState(QActor qa, String state) {
        systemState.setState(gson.fromJson(state, State.class));
        notifyUpdateState(qa);
    }

    private static void notifyUpdateState(QActor qa) {
        Gson gson = new Gson();
        String content = gson.toJson(systemState);
        System.out.println("PAYLOAD: " + content);
        // TODO
    }
}
