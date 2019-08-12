package it.unibo.robot_adapter.demo;

import it.unibo.qactors.akka.QActor;
import it.unibo.robot_adapter.IRobotExecutor;

/**
 * Simple RobotExecutor that only prints output on QActor standard output.
 */
public class DemoExecutor implements IRobotExecutor {

    @Override
    public void setUp(final QActor qa, final String args) {
        qa.println("Started demo executor");
    }

    @Override
    public void terminate(final QActor qa) {
        qa.println("Closing demo executor");
    }

    @Override
    public void doMove(final QActor qa, final String cmd) {
        qa.println("Demo robot executing commmand " + cmd);
    }

}
