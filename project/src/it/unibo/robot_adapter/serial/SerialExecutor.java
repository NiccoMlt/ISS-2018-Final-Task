package it.unibo.robot_adapter.serial;

import it.unibo.qactors.akka.QActor;
import it.unibo.robot_adapter.IRobotExecutor;

/**
 * RobotExecutor realization that controls a serial-connected Arduino.
 */
public class SerialExecutor implements IRobotExecutor {

    private RobotSerialCommunication robotSupport; // singleton
    private int printCount = 0;

    @Override
    public void setUp(final QActor qa, final String port) {
        if (this.robotSupport == null) {
            this.robotSupport = new RobotSerialCommunication(port, new RobotSerialCommunication.Logger() {
                private String owner = "";

                @Override
                public void setOwner(final String owner) {
                    this.owner = owner + " ";
                }

                @Override
                public void log(final String msg) {
                    qa.println(this.owner + msg);
                }
            });

            this.robotSupport.addObserverToSensors(data -> {
                double distance = Double.parseDouble(data);
                if (distance < 20.0 || printCount++ == 5) {
                    printCount = 0;
                    qa.println("\t sonar: " + distance);
                    qa.emit("robotSonar", "robotSonar(distance(" + data + "))");
                    if (distance < 7.0) {
                        qa.println("\t FISICAL COLLISION: " + distance);
                        qa.emit("collisionDispatch", "collisionDispatch(obstacle(fisico))");
                    }
                }
            });
        }
    }

    @Override
    public void terminate(final QActor qa) {
        robotSupport.close();
    }

    @Override
    public void doMove(final QActor qa, final String cmd) {
        qa.println("Serial robot executing command: " + cmd);
        switch (cmd) {
            case "h":
            case "w":
            case "a":
            case "s":
            case "d":
                this.robotSupport.executeTheCommand(cmd);
                break;
            case "blinkStart":
                qa.println("Started blinking");
                robotSupport.executeTheCommand("b");
                break;
            case "blinkStop":
                qa.println("Stopped blinking");
                robotSupport.executeTheCommand("n");
                break;
            default:
                qa.println("Unexpected command \"" + cmd + "\"");
        }
    }
}
