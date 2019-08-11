/* Generated by AN DISI Unibo */ 
package it.unibo.ctxRobot;
import it.unibo.qactors.QActorContext;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.system.SituatedSysKb;
public class MainCtxRobot  {
  
//MAIN
public static QActorContext initTheContext() throws Exception{
	IOutputEnvView outEnvView = SituatedSysKb.standardOutEnvView;
	String webDir = "./srcMore/it/unibo/ctxRobot";
	return QActorContext.initQActorSystem(
		"ctxrobot", "./srcMore/it/unibo/ctxRobot/robot.pl", 
		"./srcMore/it/unibo/ctxRobot/sysRules.pl", outEnvView,webDir,false);
}
public static void main(String[] args) throws Exception{
	QActorContext ctx = initTheContext();
} 	
}
