package it.unibo.utils;

import java.util.ArrayList;
import java.util.Hashtable;

import it.unibo.planning.RoomMap;
import it.unibo.planning.planUtil;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.QActorUtils;
import it.unibo.qactors.akka.QActor;
import it.unibo.systemstate.Action;
import it.unibo.systemstate.Position;
import it.unibo.systemstate.RobotState;
import it.unibo.systemstate.State;
import it.unibo.systemstate.systemStateUtil;

public class updateStateOnConsole {

	private static final Action ACTION_HOME = new Action("Go home", "standard", "cmd(home)");
	private static final Action ACTION_EXPLORE = new Action("Explore", "standard", "cmd(explore)");
	private static final Action ACTION_RETRIEVE = new Action("Retrieve bomb", "standard", "cmd(retrieve)");
	private static final Action ACTION_HALT = new Action("Stop", "standard", "cmd(halt)");
	private static final Action ACTION_BAG = new Action("Is bag", "standard", "bagStatus(bag)");
	private static final Action ACTION_BOMB = new Action("Is bomb", "standard", "bagStatus(bomb)");

	public static void updateMap(QActor qa) {
		// Updates map
		String[][] matrix = RoomMap.getRoomMap().toMatrix();

		try {
			Hashtable<String, String> res = QActorUtils.evalTheGuard(qa, " !?bomb(X,Y)");
			if (res != null) {
				matrix[Integer.parseInt(res.get("Y"))][Integer.parseInt(res.get("X"))] = "b";
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		String map = jsonUtil.encodeForProlog(matrix);
		try {
			qa.sendMsg("stateUpdate", "console", QActorContext.dispatch,
					"state(TYPE, PAYLOAD)".replace("TYPE", "map").replace("PAYLOAD", map));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Updates robotMovement
		it.unibo.planning.RobotState tmpState = planUtil.getRobotState(qa);
		String robotMovement = jsonUtil.encodeForProlog(
				new RobotState(new Position(tmpState.getX(), tmpState.getY()), tmpState.getDirection().name(), null));
		try {
			qa.sendMsg("stateUpdate", "console", QActorContext.dispatch,
					"state(TYPE, PAYLOAD)".replace("TYPE", "robotMovement").replace("PAYLOAD", robotMovement));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateRobotState(QActor qa, String state) {
		ArrayList<Action> actions = new ArrayList<>();

		switch (state) {
		case "discovery-home":
			actions.add(ACTION_EXPLORE);
			break;
		case "discovery-idle":
			actions.add(ACTION_HOME);
			actions.add(ACTION_EXPLORE);
			break;
		case "discovery-obstacle":
			actions.add(ACTION_BAG);
			actions.add(ACTION_BOMB);
			break;
		case "retriever-home":
            actions.add(ACTION_RETRIEVE);
            break;
        case "retriever-idle":
            actions.add(ACTION_HOME);
            actions.add(ACTION_RETRIEVE);
            break;
        case "retriever-idle-with-bomb":
            actions.add(ACTION_HOME);
            break;
        case "discovery-exploring":
        case "retriever-retrieving":
            actions.add(ACTION_HALT);
            break;
        case "terminating":
            break;
		default:
			actions.add(ACTION_HALT);
		}

		String payload = jsonUtil.encodeForProlog(new State(state, actions.toArray(new Action[0])));
		try {
			qa.sendMsg("stateUpdate", "console", QActorContext.dispatch,
					"state(TYPE, PAYLOAD)".replace("TYPE", "robotState").replace("PAYLOAD", payload));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void receivedUpdateState(QActor qa, String type, String payload) {
		switch (type) {
		case "temperature":
			systemStateUtil.getSystemStateUtil().updateTemperature(qa, payload);
			break;
		case "map":
			systemStateUtil.getSystemStateUtil().updateMap(qa, jsonUtil.decodeFromProlog(payload, String[][].class));
			break;
		case "robotMovement":
			systemStateUtil.getSystemStateUtil().updateRobotMovement(qa, jsonUtil.decodeFromProlog(payload, RobotState.class));
			break;
		case "robotState":
			systemStateUtil.getSystemStateUtil().updateRobotState(qa, jsonUtil.decodeFromProlog(payload, State.class));
			break;
		}
	}
}
