package it.unibo.robotadapter;

import it.unibo.qactors.akka.QActor;

/** The interface models an instance of a robot that can execute commands. */
public interface IRobotExecutor {

    /**
     * The setup method initializes the Robot Executor.
     *
     * @param qa   the actor
     * @param args the arguments
     */
    void setUp(QActor qa, String args);

    /**
     * The method terminate the Robot Executor.
     *
     * @param qa the actor
     */
    void terminate(QActor qa);

    /**
     * The method sends a string command to the Executor to move the Robot.
     *
     * @param qa  the actor
     * @param cmd the command to execute
     */
    void doMove(QActor qa, String cmd);

}
