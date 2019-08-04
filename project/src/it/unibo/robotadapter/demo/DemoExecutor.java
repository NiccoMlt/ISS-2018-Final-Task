package it.unibo.robotadapter.demo;

import it.unibo.qactors.akka.QActor;
import it.unibo.robotadapter.RobotExecutor;

/**
 * Simple RobotExecutor that only prints output on QActor standard output.
 */
public class DemoExecutor implements RobotExecutor {

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
		qa.println("Executing commmand " + cmd);
	}

}
