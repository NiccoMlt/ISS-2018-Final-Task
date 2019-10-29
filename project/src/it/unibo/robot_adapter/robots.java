package it.unibo.robot_adapter;

import it.unibo.qactors.QActorContext;
import it.unibo.qactors.akka.QActor;
import it.unibo.robot_adapter.demo.DemoExecutor;
import it.unibo.robot_adapter.serial.SerialExecutor;
import it.unibo.robot_adapter.virtual.VirtualExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class robots {

    public static enum Type {
        ROBOT_SERIAL("robotSerial"), ROBOT_DEMO("robotDemo"), ROBOT_VIRTUAL("robotVirtual");

        private final String name;

        private Type(final String name) {
            this.name = name;
        }

        public static final Type parseString(final String type) throws IllegalArgumentException {
            return Arrays.asList(Type.values()).stream().filter(t -> t.name.equals(type)).findFirst()
                    .orElseGet(() -> Type.valueOf(type));
        }
    }

    private static final HashMap<Type, ArrayList<IRobotExecutor>> executors = new HashMap<>();

    /**
     * Setup a robot executor.
     *
     * @param qa        the actor
     * @param robotType the type of robot
     * @param args      the arguments
     */
    public static void setUp(final QActor qa, final String robotType, final String args) {
        try {
            Optional<IRobotExecutor> opt = Optional.empty();
            final Type enumType = Type.parseString(robotType);
            switch (enumType) {
            case ROBOT_SERIAL:
                opt = Optional.of(new SerialExecutor());
                break;
            case ROBOT_DEMO:
                opt = Optional.of(new DemoExecutor());
                break;
            case ROBOT_VIRTUAL:
                opt = Optional.of(new VirtualExecutor());
                break;
            default:
                qa.println("Can't find executor for robot type: " + enumType.name);
            }
            opt.ifPresent(executor -> {
                qa.println("Setup robot \"" + enumType.name + "\" with args=" + args);
                executors.putIfAbsent(enumType, new ArrayList<>());
                if (args.startsWith("\'")) {
                    executor.setUp(qa, args.substring(1, args.length() - 1));
                } else {
                    executor.setUp(qa, args);
                }
                executors.get(enumType).add(executor);
            });
        } catch (final Exception e) {
            qa.println("Exception " + e.toString() + " during setup of robot " + robotType);
        }
    }

    /**
     * Terminate all stored executors and remove them.
     *
     * @param qa the actor
     */
    public static void terminate(final QActor qa) {
        qa.println("all robots terminating");
        for (final Type key : executors.keySet()) {
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
     * @param qa  the actor
     * @param cmd the command to execute
     */
    public static void doMove(final QActor qa, final String cmd) { // Args MUST be String
        try {
            executors.values().forEach(execs -> execs.forEach(executor -> executor.doMove(qa, cmd)));
            sendCommandCompleted(qa, cmd);
        } catch (final Exception e) {
            System.out.println( "movePlanUtil ERROR:" + e.getMessage() );
        }
    }

    public static void doMove(QActor qa, String cmd, String duration) { // Args MUST be String
        try {
            executors.values().forEach(execs -> execs.forEach(executor -> executor.doMove(qa, cmd)));
            Thread.sleep(Integer.parseInt(duration));
            executors.values().forEach(execs -> execs.forEach(executor -> executor.doMove(qa, "h")));
            sendCommandCompleted(qa, cmd);
        } catch (final Exception e) {
            System.out.println( "movePlanUtil ERROR:" + e.getMessage() );
        }

    }

    private static void sendCommandCompleted(QActor qa, String cmd) throws Exception {
        switch (cmd) {
            case "a":
            case "d":
                final String temporaryStr = "moveMsgCmdDone(CMD)".replace("CMD", cmd);
                qa.sendMsg("moveMsgCmdDone","robot_advanced", QActorContext.dispatch, temporaryStr);
                break;
            default:
        }
    }
}
