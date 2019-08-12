package it.unibo;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import it.unibo.ctxRobot.MainCtxRobot;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.QActorUtils;
import it.unibo.qactors.akka.QActor;

public class TestRBlinkLed {
    private QActorContext context;
    private QActor mind;
    
    @Before
    public void setUp() throws Exception {
        context = MainCtxRobot.initTheContext();
        System.out.println(" ***TEST:*** TestRBlinkLed waits for a while ........ ");
        Thread.sleep(3000);
        mind = QActorUtils.getQActor("robot_discovery_mind_ctrl");
        QActorUtils.getQActor("world_observer_ctrl");
        System.out.println(Collections.list(QActorUtils.qActorTable.keys()));
        System.out.println(mind);
    }

    @Test
    public void testRBlinkLed() throws InterruptedException {
        // R-blinkLed (start)
        try {
            QActorUtils.sendMsg(mind, "robot_discovery_mind", "cmdExplore", "cmdExplore");
            Thread.sleep(500);
            final SolveInfo sol = mind.solveGoal("ledState(X)");
            final String ledState = sol.getVarValue("X").toString();
            System.out.println(ledState);
            assertTrue(ledState.equals("blinking"));
        } catch (final NoSolutionException e) {
            fail(e.getMessage());
        }

        // R-blinkLed (stop)
        try {
            QActorUtils.sendMsg(mind, "robot_discovery_mind", "cmdStop", "cmdStop");
            Thread.sleep(2000);
            final SolveInfo sol = mind.solveGoal("ledState(X)");
            final String ledState = sol.getVarValue("X").toString();
            assertTrue(ledState.equals("off"));
        } catch (final NoSolutionException e) {
            fail(e.getMessage());
        }
    }
    
    @After
    public void shutdown() throws Exception {
        if(context != null) {
            context.terminateQActorSystem();
        }
    }
}