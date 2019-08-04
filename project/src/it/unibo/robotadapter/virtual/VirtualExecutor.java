package it.unibo.robotadapter.virtual;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import it.unibo.qactors.akka.QActor;
import it.unibo.robotadapter.RobotExecutor;

/** TODO */
public class VirtualExecutor implements RobotExecutor {
    private static final String DEFAULT_PORT = "8999";

    private Process process;
    private boolean soffrittiStarted = false;

    @Override
    public void setUp(final QActor qa, final String args) { // hostName, startWithSoffritti
        final String[] splitted = args.split(",");
        if (splitted.length == 2) {
            try {
                qa.println("robotVirtual setUp " + splitted[0]);
                if (Boolean.parseBoolean(splitted[1])) {
                    startSoffritti(qa);
                }
                qa.println("opening connection...");
                VirtualRobotTcpClient.initClientConn(qa, splitted[0], DEFAULT_PORT);
            } catch (final Exception e) {
                e.printStackTrace();
                terminate(qa);
            }
        }
    }

    @Override
    public void terminate(final QActor qa) {
        VirtualRobotTcpClient.terminate();
        terminateSoffritti(qa);
    }

    /**
     * Log on standard output the output of a process.
     * 
     * @param proc the process to get output from
     * @param log  the logger to use
     * @throws IOException is something goes wrong
     */
    private void printProcessOutput(final Process proc, final Logger log) throws IOException {
        final BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            log.line(s);
        }
    }

    /**
     * Start Soffritti virtual environment server.
     * 
     * @param qa the actor
     * @throws Exception if something goes wrong
     */
    private void startSoffritti(final QActor qa) throws Exception {
        qa.println("starting server...");
        this.process = Runtime.getRuntime().exec("./startServer.sh", null, new File("srcMore/Soffritti"));
        new Thread() {
            @Override
            public void run() {
                try {
                    printProcessOutput(process, line -> {
                        System.out.println(line);
                        if (line.contains("listening")) {
                            soffrittiStarted = true;
                        }
                    });
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        if (!process.isAlive() && process.exitValue() != 0) {
            throw new Exception("Error during execution, install node and its modules!");
        } else {
            while (!soffrittiStarted) {
                Thread.sleep(500);
            }
        }
    }

    /**
     * Shutdown Soffritti virtual environment server.
     * 
     * @param qa the actor
     */
    private void terminateSoffritti(final QActor qa) {
        if (process != null) {
            qa.println("closing server...");
            process.destroy();
        } else {
            qa.println("server not started...");
        }
    }

    @Override
    public void doMove(QActor qa, String cmd) {
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
            // TODO
        }
    }

    /** Virtual executor logger interface. */
    private interface Logger {

        /**
         * Log a line.
         * 
         * @param s the line to print
         */
        void line(final String s);
    }
}
