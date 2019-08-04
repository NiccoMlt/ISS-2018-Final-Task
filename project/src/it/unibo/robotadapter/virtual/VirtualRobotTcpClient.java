package it.unibo.robotadapter.virtual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;
import it.unibo.qactors.akka.QActor;

/** TODO */
public class VirtualRobotTcpClient {
    private static final String DEFAULT_HOST_NAME = "localhost";
    private static final int DEFAULT_PORT = 8999;

    private static String hostName = DEFAULT_HOST_NAME;
    private static int port = DEFAULT_PORT;
    private static final String SEP = ";";
    protected static Socket clientSocket;
    protected static PrintWriter outToServer;
    protected static BufferedReader inFromServer;

    /**
     * Initialize Client connection.
     * <p>
     * It uses default hostName ({@value #DEFAULT_HOST_NAME}) and port
     * ({@value #DEFAULT_PORT}).
     *
     * @param qa the actor
     *
     * @see #initClientConn(QActor, String, String)
     */
    public static void initClientConn(final QActor qa) {
        initClientConn(qa, hostName, "" + port);
    }

    /**
     * Initialize Client connection.
     * 
     * @param qa          the actor
     * @param hostNameStr the host name
     * @param portStr     the port
     */
    public static void initClientConn(final QActor qa, final String hostNameStr, final String portStr) {
        try {
            hostName = hostNameStr;
            port = Integer.parseInt(portStr);
            clientSocket = new Socket(hostName, port);
            // outToServer = new DataOutputStream(clientSocket.getOutputStream()); // DOES NOT WORK!!!!;
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer = new PrintWriter(clientSocket.getOutputStream());
            startTheReader(qa);
        } catch (final Exception e) {
            qa.println("	$$$clientTcpForVirtualRobot ERROR " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Close the TCP socket connection. */
    public static void terminate() {
        try {
            clientSocket.close();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Send a message through TCP connection.
     *
     * @param qa the actor
     * @param jsonString the JSON string message
     */
    public static void sendMsg(final QActor qa, final String jsonString) {
        try {
            final JSONObject jsonObject = new JSONObject(jsonString);
            final String msg = SEP + jsonObject.toString() + SEP;
            outToServer.println(msg);
            outToServer.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Start Reader.
     * 
     * @param qa the actor
     */
    protected static void startTheReader(final QActor qa) {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        final String inpuStr = inFromServer.readLine();
                        // System.out.println( "reads: " + inpuStr);
                        final String jsonMsgStr = inpuStr.split(";")[1];
                        // System.out.println( "reads: " + jsonMsgStr + " qa=" + qa.getName() );
                        final JSONObject jsonObject = new JSONObject(jsonMsgStr);
                        // System.out.println( "type: " + jsonObject.getString("type"));
                        switch (jsonObject.getString("type")) {
                            case "webpage-ready":
                                System.out.println("wenv ready ");
                                break;
                            case "sonar-activated": {
                                // wSystem.out.println( "sonar-activated " );
                                final JSONObject jsonArg = jsonObject.getJSONObject("arg");
                                final String sonarName = jsonArg.getString("sonarName");
                                final int distance = jsonArg.getInt("distance");
    //							System.out.println( "sonarName=" +  sonarName + " distance=" + distance);
                                qa.emit("robotSonarWall", "sonar(NAME, player, DISTANCE)"
                                            .replace("NAME", sonarName.replace("-", ""))
                                            .replace("DISTANCE", ("" + distance)));
                                break;
                            }
                            case "collision": {
                                // System.out.println( "collision" );
                                final JSONObject jsonArg = jsonObject.getJSONObject("arg");
                                final String objectName = jsonArg.getString("objectName");
                                System.out.println("collision objectName=" + objectName);
                                qa.emit("robotSonarObstacle", "obstacle(TARGET)".replace("TARGET", objectName)); // objectName.replace("-", "")
                                break;
                            }
                        }
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}
