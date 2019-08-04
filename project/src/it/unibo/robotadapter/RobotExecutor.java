package it.unibo.robotadapter;

import it.unibo.qactors.akka.QActor;

public interface RobotExecutor {

    void setUp(QActor qa, String args);

    void terminate(QActor qa);

    void doMove(QActor qa, String cmd);

}
