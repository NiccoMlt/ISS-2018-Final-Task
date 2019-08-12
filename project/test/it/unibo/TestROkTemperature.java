package it.unibo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import alice.tuprolog.SolveInfo;
import it.unibo.ctxRobot.MainCtxRobot;
import it.unibo.qactors.QActorUtils;
import it.unibo.qactors.akka.QActor;

public class TestROkTemperature {
    private QActor worldobserver;

    @Before
    public void setUp() throws Exception {
        MainCtxRobot.initTheContext();
        System.out.println(" ***TEST:*** TestROkTemperature waits for a while ........ ");
        Thread.sleep(3000);
        QActorUtils.getQActor("robot_discovery_mind_ctrl");
        worldobserver = QActorUtils.getQActor("world_observer_ctrl");
        System.out.println(Collections.list(QActorUtils.qActorTable.keys()));
    }

    @Test
    public void testRTempOk() throws InterruptedException {
        QActorUtils.emitEventAfterTime(worldobserver, "world_observer", "temperature", "temperature(18)", 100);
        Thread.sleep(500);
        SolveInfo sol = worldobserver.solveGoal("evaluateEnvironment");
        boolean tempOk = sol.isSuccess();
        System.out.println(tempOk);
        assertTrue(tempOk);

        QActorUtils.emitEventAfterTime(worldobserver, "world_observer", "temperature", "temperature(35)", 100);
        Thread.sleep(500);
        sol = worldobserver.solveGoal("evaluateEnvironment");
        tempOk = sol.isSuccess();
        System.out.println(tempOk);
        assertFalse(tempOk);
    }
}