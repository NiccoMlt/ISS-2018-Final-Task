package it.unibo.robotadapter.serial;

import it.unibo.qactors.akka.QActor;
import it.unibo.robotadapter.IRobotExecutor;

/**
 * RobotExecutor realization that controls a serial-connected Arduino.
 */
public class SerialExecutor implements IRobotExecutor {

    private RobotSerialCommunication robotSupport;

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

            this.robotSupport.addObserverToSensors(data ->
                qa.emit("robotSonar", "robotSonar(distance(" + data + "))"));
        }
    }

    @Override
    public void terminate(final QActor qa) {
        robotSupport.close();
    }

    @Override
    public void doMove(final QActor qa, final String cmd) {
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
                break;
            case "blinkStop":
                qa.println("Stopped blinking");
                break;
            default:
                qa.println("Unexpected command \"" + cmd + "\""); // TODO
        }
    }
}
