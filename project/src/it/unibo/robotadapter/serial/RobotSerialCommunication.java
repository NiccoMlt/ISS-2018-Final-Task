package it.unibo.robotadapter.serial;

import it.unibo.robotadapter.IRobotSerialObserver;
import it.unibo.supports.serial.JSSCSerialComm;
import it.unibo.supports.serial.SerialPortConnSupport;
import java.util.ArrayList;
import java.util.List;

/** TODO: documentation */
public class RobotSerialCommunication {

    private final List<IRobotSerialObserver> observers = new ArrayList<>();
    private final Logger logger;
    private final String port;
    private SerialPortConnSupport conn = null;
    private JSSCSerialComm serialConn;
    private double dataSonar = 0;
    private String curDataFromArduino;
    // private Thread observerThread = null; // TODO

    /**
     * Constructor.
     *
     * @param port   the port to use
     * @param logger the logger to use
     */
    public RobotSerialCommunication(final String port, final Logger logger) {
        this.logger = logger;
        this.logger.setOwner("RobotSerialCommunication");
        this.port = port;
        try {
            this.logger.log("start");
            this.serialConn = new JSSCSerialComm(null);
            this.conn = this.serialConn.connect(port); // returns a SerialPortConnSupport
            if (this.conn == null) {
                this.logger.log("Null connection");
                // return now
            } else {
                this.curDataFromArduino = conn.receiveALine();
                this.logger.log("received: " + dataSonar);
                this.getDataFromArduino();
            }
        } catch (final Exception e) {
            this.logger.log("ERROR " + e.getMessage());
        }
    }

    /** Close the serial connection. */
    public void close() {
        this.logger.log("closing communication on port " + this.port);
        try {
            this.conn.closeConnection();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a command message to the executor connected over serial.
     *
     * @param cmd the command for the executor
     */
    public void executeTheCommand(final String cmd) {
        this.logger.log("executeTheCommand " + cmd + " conn=" + this.conn);
        try {
            this.serialConn.writeData(cmd);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Register an observer to the robot executor.
     *
     * @param observer the RobotSerial Observer to register
     */
    public void addObserverToSensors(final IRobotSerialObserver observer) {
        this.observers.add(observer);
    }

    /**
     * Asynchronously retrieve data from a serial-connected Arduino.
     * <p>
     * It should be invoked once.
     */
    private void getDataFromArduino() {
        new Thread(() -> {
            try {
                RobotSerialCommunication.this.logger.log("getDataFromArduino STARTED");
                while (true) {
                    try {
                        RobotSerialCommunication.this.curDataFromArduino = conn.receiveALine();
// 	 						logger.log("mbotConnArduinoObj received:" + curDataFromArduino );
                        final double v = Double.parseDouble(RobotSerialCommunication.this.curDataFromArduino);
                        // handle too fast change
                        final double delta = Math.abs(v - RobotSerialCommunication.this.dataSonar);
                        if (delta < 7 && delta > 0.5) {
                            RobotSerialCommunication.this.dataSonar = v;
                            for (final IRobotSerialObserver observer : RobotSerialCommunication.this.observers) {
                                observer.notify("" + RobotSerialCommunication.this.dataSonar);
                            }
//								QActorUtils.raiseEvent(curActor, curActor.getName(), "realSonar",
//										"sonar( DISTANCE )".replace("DISTANCE", ""+dataSonar ));
                        }
                    } catch (final Exception e) {
                        RobotSerialCommunication.this.logger.log("ERROR:" + e.getMessage());
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /** Logger object abstraction interface for this class. */
    public interface Logger {
        /**
         * Set the owner name.
         *
         * @param owner the logger owner name
         */
        void setOwner(String owner);

        /**
         * Log something.
         *
         * @param msg the message to log
         */
        void log(String msg);
    }
}
