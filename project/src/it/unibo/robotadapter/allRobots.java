package it.unibo.robotadapter;

import java.util.ArrayList;
import java.util.HashMap;

import it.unibo.qactors.akka.QActor;
import it.unibo.robotadapter.demo.DemoExecutor;
import it.unibo.robotadapter.serial.SerialExecutor;
import it.unibo.robotadapter.virtual.VirtualExecutor;

public class allRobots {

    // TODO: replace with enum
    public static final String ROBOT_SERIAL = "robotSerial";
    public static final String ROBOT_DEMO = "robotDemo";
    public static final String ROBOT_VIRTUAL = "robotVirtual";

    private static final HashMap<String, ArrayList<RobotExecutor>> executors = new HashMap<>();

    /**
     * Setup a robot executor.
     * 
     * @param qa the actor
     * @param robotType the type of robot
     * @param args the arguments
     */
    public static void setUp(final QActor qa, final String robotType, final String args) {
        try {
            RobotExecutor tmp = null;
            switch (robotType) {
                case ROBOT_SERIAL:
                    tmp = new SerialExecutor();
                    break;
                case ROBOT_DEMO:
                    tmp = new DemoExecutor();
                    break;
                case ROBOT_VIRTUAL:
                    tmp = new VirtualExecutor();
                    break;
                default:
                    // TODO
            }
            qa.println("allRobots " + robotType + " setUp args=" + args);
            executors.putIfAbsent(robotType, new ArrayList<>());
            if (tmp != null) {
                if (args.startsWith("\'"))
                    tmp.setUp(qa, args.substring(1, args.length() - 1));
                else
                    tmp.setUp(qa, args);
                executors.get(robotType).add(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Terminate all stored executors.
     * 
     * @param qa the actor
     */
    public static void terminate(final QActor qa) {
        // Termina gli executor e li rimuove
        qa.println("allRobots terminating");
        for (final String key : executors.keySet()) {
            for (int i = executors.get(key).size() - 1; i >= 0; i--) {
                executors.get(key).get(i).terminate(qa);
                executors.get(key).remove(i);
            }
            executors.remove(key);
        }
    }

    /**
     * Do a move to all the memorized executors.
     * 
     * @param qa the actor
     * @param cmd the command to execute
     */
    public static void doMove(final QActor qa, final String cmd) { // Args MUST be String
        try {
            executors.values().forEach(execs -> execs.forEach(executor -> executor.doMove(qa, cmd)));
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }
}
