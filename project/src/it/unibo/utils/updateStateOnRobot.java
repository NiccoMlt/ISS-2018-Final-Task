package it.unibo.utils;

import it.unibo.planning.Box;
import it.unibo.planning.RoomMap;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.akka.QActor;
import it.unibo.systemstate.systemStateUtil;

public class updateStateOnRobot {

    public static void loadMap(QActor qa, String jsonMap) {
        String[][] map = jsonUtil.decodeFromProlog(jsonMap, String[][].class);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                switch (map[y][x]) {
                    case "1":
                        RoomMap.getRoomMap().put(x, y, new Box(false, false));
                        break;
                    case "b":
                        // save bomb position
                        qa.addRule("bomb(X,Y)".replace("X", String.valueOf(x)).replace("Y", String.valueOf(y)));
                    case "0":// Dirty as obstacle, so planner doesn't pass there
                    case "x":
                        RoomMap.getRoomMap().put(x, y, new Box(true, false));
                        break;
                    case "r":
                        RoomMap.getRoomMap().put(x, y, new Box(false, false, true));
                        break;
                }
            }
        }
    }
    
    public static void sendMap(QActor qa) {
        String payload = jsonUtil.encodeForProlog(systemStateUtil.getSystemState().getWorld().getMap());
        try {
            qa.sendMsg("map", "robot_retriever_mind", QActorContext.dispatch, "map(M)".replace("M", payload));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
