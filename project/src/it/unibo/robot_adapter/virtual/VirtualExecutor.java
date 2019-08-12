package it.unibo.robot_adapter.virtual;

import it.unibo.qactors.akka.QActor;
import it.unibo.robot_adapter.IRobotExecutor;

/** TODO */
public class VirtualExecutor implements IRobotExecutor {
    private static final String DEFAULT_PORT = "8999";

    @Override
    public void setUp(final QActor qa, final String args) { // hostName
        try {
            qa.println("robotVirtual setUp " + args);
            qa.println("opening connection...");
            VirtualRobotTcpClient.initClientConn(qa, args, DEFAULT_PORT);
        } catch (final Exception e) {
            e.printStackTrace();
            terminate(qa);
        }
    }

    @Override
    public void terminate(final QActor qa) {
        VirtualRobotTcpClient.terminate();
    }

    @Override
    public void doMove(final QActor qa, final String cmd) {
        qa.println("Virtual robot executing command: " + cmd);
        switch (cmd) {
            case "h":
                VirtualRobotTcpClient.sendMsg(qa, "{'type': 'alarm', 'arg': 0 }");
                break;
            case "w":
                VirtualRobotTcpClient.sendMsg(qa, "{'type': 'moveForward', 'arg': -1 }");
                break;
            case "a":
                VirtualRobotTcpClient.sendMsg(qa, "{'type': 'turnLeft', 'arg': 400 }");
                break;
            case "s":
                VirtualRobotTcpClient.sendMsg(qa, "{'type': 'moveBackward', 'arg': -1 }");
                break;
            case "d":
                VirtualRobotTcpClient.sendMsg(qa, "{'type': 'turnRight', 'arg': 400 }");
                break;
            case "blinkStart":
                qa.println("Started blinking");
                break;
            case "blinkStop":
                qa.println("Stopped blinking");
                break;
            default:
                qa.println("Unexpected command \"" + cmd + "\"");
        }
    }
}
