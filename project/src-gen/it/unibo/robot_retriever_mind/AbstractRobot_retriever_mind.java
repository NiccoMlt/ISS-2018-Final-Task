/* Generated by AN DISI Unibo */ 
package it.unibo.robot_retriever_mind;
import it.unibo.qactors.PlanRepeat;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.StateExecMessage;
import it.unibo.qactors.QActorUtils;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.action.AsynchActionResult;
import it.unibo.qactors.action.IActorAction;
import it.unibo.qactors.action.IActorAction.ActionExecMode;
import it.unibo.qactors.action.IMsgQueue;
import it.unibo.qactors.akka.QActor;
import it.unibo.qactors.StateFun;
import java.util.Stack;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.qactors.action.ActorTimedAction;
public abstract class AbstractRobot_retriever_mind extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	//protected String mqttServer = "";
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractRobot_retriever_mind(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/robot_retriever_mind/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/robot_retriever_mind/plans.txt";
	  	}
		@Override
		protected void doJob() throws Exception {
			String name  = getName().replace("_ctrl", "");
			mysupport = (IMsgQueue) QActorUtils.getQActor( name ); 
			initStateTable(); 
	 		initSensorSystem();
	 		history.push(stateTab.get( "init" ));
	  	 	autoSendStateExecMsg();
	  		//QActorContext.terminateQActorSystem(this);//todo
		} 	
		/* 
		* ------------------------------------------------------------
		* PLANS
		* ------------------------------------------------------------
		*/    
	    //genAkkaMshHandleStructure
	    protected void initStateTable(){  	
	    	stateTab.put("handleToutBuiltIn",handleToutBuiltIn);
	    	stateTab.put("init",init);
	    	stateTab.put("home",home);
	    	stateTab.put("checkTemperatureAndRetrieve",checkTemperatureAndRetrieve);
	    	stateTab.put("goToReachBomb",goToReachBomb);
	    	stateTab.put("goToIdle",goToIdle);
	    	stateTab.put("goToHome",goToHome);
	    	stateTab.put("idle",idle);
	    	stateTab.put("doActions",doActions);
	    	stateTab.put("waitMoveCompletedAnswer",waitMoveCompletedAnswer);
	    	stateTab.put("handleCmdDone",handleCmdDone);
	    	stateTab.put("backToHome",backToHome);
	    	stateTab.put("terminate",terminate);
	    	stateTab.put("handleError",handleError);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "robot_retriever_mind tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("init",-1);
	    	String myselfName = "init";  
	    	it.unibo.planning.planUtil.initAI( myself  );
	    	//switchTo home
	        switchToPlanAsNextState(pr, myselfName, "robot_retriever_mind_"+myselfName, 
	              "home",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun home = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_home",0);
	     pr.incNumIter(); 	
	    	String myselfName = "home";  
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_mind_"+myselfName,false,
	          new StateFun[]{() -> {	//AD HOC state to execute an action and resumeLastPlan
	          try{
	            PlanRepeat pr1 = PlanRepeat.setUp("adhocstate",-1);
	            //ActionSwitch for a message or event
	             if( currentEvent.getMsg().startsWith("environment") ){
	            	String parg="replaceRule(environment(X),environment(E))";
	            	/* PHead */
	            	parg =  updateVars( Term.createTerm("environment(X)"), 
	            	                    Term.createTerm("environment(E)"), 
	            		    		  	Term.createTerm(currentEvent.getMsg()), parg);
	            		if( parg != null ) {
	            		    aar = QActorUtils.solveGoal(this,myCtx,pengine,parg,"",outEnvView,86400000);
	            			//println(getName() + " plan " + curPlanInExec  +  " interrupted=" + aar.getInterrupted() + " action goon="+aar.getGoon());
	            			if( aar.getInterrupted() ){
	            				curPlanInExec   = "home";
	            				if( aar.getTimeRemained() <= 0 ) addRule("tout(demo,"+getName()+")");
	            				if( ! aar.getGoon() ) return ;
	            			} 			
	            			if( aar.getResult().equals("failure")){
	            				if( ! aar.getGoon() ) return ;
	            			}else if( ! aar.getGoon() ) return ;
	            		}
	             }
	            repeatPlanNoTransition(pr1,"adhocstate","adhocstate",false,true);
	          }catch(Exception e ){  
	             println( getName() + " plan=home WARNING:" + e.getMessage() );
	             //QActorContext.terminateQActorSystem(this); 
	          }
	          },
	           () -> {	//AD HOC state to execute an action and resumeLastPlan
	          try{
	            PlanRepeat pr1 = PlanRepeat.setUp("adhocstate",-1);
	            //ActionSwitch for a message or event
	             if( currentMessage.msgContent().startsWith("map") ){
	            	{/* JavaLikeMove */ 
	            	String arg1 = "M" ;
	            	arg1 =  updateVars( Term.createTerm("map(M)"), Term.createTerm("map(M)"), 
	            		                Term.createTerm(currentMessage.msgContent()),  arg1 );	                
	            	//end arg1
	            	it.unibo.utils.updateStateOnRobot.loadMap(this,arg1 );
	            	}
	             }
	            repeatPlanNoTransition(pr1,"adhocstate","adhocstate",false,true);
	          }catch(Exception e ){  
	             println( getName() + " plan=home WARNING:" + e.getMessage() );
	             //QActorContext.terminateQActorSystem(this); 
	          }
	          },
	           stateTab.get("checkTemperatureAndRetrieve") }, 
	          new String[]{"true","E","environment", "true","M","map", "true","M","cmdReachBomb" },
	          6000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_home){  
	    	 println( getName() + " plan=home WARNING:" + e_home.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//home
	    
	    StateFun checkTemperatureAndRetrieve = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("checkTemperatureAndRetrieve",-1);
	    	String myselfName = "checkTemperatureAndRetrieve";  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?environment(ok)" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmdReachBomb","cmdReachBomb", guardVars ).toString();
	    	sendMsg("cmdReachBomb",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("goToReachBomb") }, 
	          new String[]{"true","M","cmdReachBomb" },
	          100, "home" );//msgTransition
	    }catch(Exception e_checkTemperatureAndRetrieve){  
	    	 println( getName() + " plan=checkTemperatureAndRetrieve WARNING:" + e_checkTemperatureAndRetrieve.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//checkTemperatureAndRetrieve
	    
	    StateFun goToReachBomb = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("goToReachBomb",-1);
	    	String myselfName = "goToReachBomb";  
	    	temporaryStr = "\"RETRIEVER_MIND[goToReachBomb] ...\"";
	    	println( temporaryStr );  
	    	it.unibo.utils.updateStateOnConsole.updateRobotState( myself ,"retriever-retrieving"  );
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?bomb(X,Y)" )) != null ){
	    	it.unibo.planning.planUtil.setGoal( myself ,guardVars.get("X"), guardVars.get("Y")  );
	    	}
	    	it.unibo.planning.planUtil.doPlan( myself  );
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_mind_"+myselfName,false,
	          new StateFun[]{}, 
	          new String[]{},
	          1000, "doActions" );//msgTransition
	    }catch(Exception e_goToReachBomb){  
	    	 println( getName() + " plan=goToReachBomb WARNING:" + e_goToReachBomb.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//goToReachBomb
	    
	    StateFun goToIdle = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("goToIdle",-1);
	    	String myselfName = "goToIdle";  
	    	temporaryStr = "\"RETRIEVER_MIND[goToIdle] ...\"";
	    	println( temporaryStr );  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?nearBomb" )) != null ){
	    	{//actionseq
	    	it.unibo.utils.updateStateOnConsole.updateRobotState( myself ,"retriever-retrieving"  );
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(3000,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "goToIdle";
	    	if( ! aar.getGoon() ) return ;
	    	temporaryStr = "bomb(_,_)";
	    	removeRule( temporaryStr );  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"endAction","endAction", guardVars ).toString();
	    	sendMsg("endAction",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	else{ {//actionseq
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?bombInRoom" )) != null ){
	    	{//actionseq
	    	it.unibo.utils.updateStateOnConsole.updateRobotState( myself ,"retriever-idle"  );
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"robotCmd(M)","robotCmd(blinkStop)", guardVars ).toString();
	    	sendMsg("robotCmd","robot_adapter", QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	else{ {//actionseq
	    	it.unibo.utils.updateStateOnConsole.updateRobotState( myself ,"retriever-idle-with-bomb"  );
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"robotCmd(M)","robotCmd(blinkStop)", guardVars ).toString();
	    	sendMsg("robotCmd","robot_adapter", QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("goToHome") }, 
	          new String[]{"true","M","endAction" },
	          100, "idle" );//msgTransition
	    }catch(Exception e_goToIdle){  
	    	 println( getName() + " plan=goToIdle WARNING:" + e_goToIdle.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//goToIdle
	    
	    StateFun goToHome = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("goToHome",-1);
	    	String myselfName = "goToHome";  
	    	temporaryStr = "\"RETRIEVER_MIND[goToHome] ...\"";
	    	println( temporaryStr );  
	    	it.unibo.utils.updateStateOnConsole.updateRobotState( myself ,"retriever-home"  );
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"robotCmd(M)","robotCmd(blinkStart)", guardVars ).toString();
	    	sendMsg("robotCmd","robot_adapter", QActorContext.dispatch, temporaryStr ); 
	    	//switchTo backToHome
	        switchToPlanAsNextState(pr, myselfName, "robot_retriever_mind_"+myselfName, 
	              "backToHome",false, false, null); 
	    }catch(Exception e_goToHome){  
	    	 println( getName() + " plan=goToHome WARNING:" + e_goToHome.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//goToHome
	    
	    StateFun idle = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("idle",-1);
	    	String myselfName = "idle";  
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("idle"), stateTab.get("goToReachBomb"), stateTab.get("goToHome") }, 
	          new String[]{"true","M","cmdStop", "true","M","cmdReachBomb", "true","M","cmdGoHome" },
	          6000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_idle){  
	    	 println( getName() + " plan=idle WARNING:" + e_idle.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//idle
	    
	    StateFun doActions = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_doActions",0);
	     pr.incNumIter(); 	
	    	String myselfName = "doActions";  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?move(M)" )) != null ){
	    	temporaryStr = "doActions_doingTheMove(M)";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " not !?move(M)" )) != null )
	    	{
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"endAction","endAction", guardVars ).toString();
	    	sendMsg("endAction",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	}
	    	{
	    	String tStr1 = "moveDuration(_)";
	    	String tStr2 = "moveDuration(moveWDuration(0))";
	    	 replaceRule( tStr1, tStr2 );  
	    	 }
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?move(M)" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"waitMoveCompleted","waitMoveCompleted", guardVars ).toString();
	    	sendMsg("waitMoveCompleted",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?doTheMove(M)" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"robotCmd(M)","robotCmd(M)", guardVars ).toString();
	    	sendMsg("robotCmd","robot_advanced", QActorContext.dispatch, temporaryStr ); 
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("goToIdle"), stateTab.get("waitMoveCompletedAnswer"), stateTab.get("backToHome") }, 
	          new String[]{"true","M","cmdStop", "true","M","waitMoveCompleted", "true","M","endAction" },
	          6000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_doActions){  
	    	 println( getName() + " plan=doActions WARNING:" + e_doActions.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//doActions
	    
	    StateFun waitMoveCompletedAnswer = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("waitMoveCompletedAnswer",-1);
	    	String myselfName = "waitMoveCompletedAnswer";  
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("goToIdle"), stateTab.get("goToIdle"), stateTab.get("handleCmdDone") }, 
	          new String[]{"true","M","moveMsgCmdObstacle", "true","M","cmdStop", "true","M","moveMsgCmdDone" },
	          6000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_waitMoveCompletedAnswer){  
	    	 println( getName() + " plan=waitMoveCompletedAnswer WARNING:" + e_waitMoveCompletedAnswer.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//waitMoveCompletedAnswer
	    
	    StateFun handleCmdDone = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleCmdDone",-1);
	    	String myselfName = "handleCmdDone";  
	    	printCurrentMessage(false);
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("moveMsgCmdDone(X)");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveMsgCmdDone") && 
	    		pengine.unify(curT, Term.createTerm("moveMsgCmdDone(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		{/* JavaLikeMove */ 
	    		String arg1 = "X" ;
	    		arg1 =  updateVars( Term.createTerm("moveMsgCmdDone(X)"), Term.createTerm("moveMsgCmdDone(X)"), 
	    			                Term.createTerm(currentMessage.msgContent()),  arg1 );	                
	    		//end arg1
	    		it.unibo.planning.planUtil.doMove(this,arg1 );
	    		}
	    	}
	    	it.unibo.planning.planUtil.showMap( myself  );
	    	it.unibo.utils.updateStateOnConsole.updateMap( myself  );
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("goToIdle") }, 
	          new String[]{"true","M","cmdStop" },
	          100, "doActions" );//msgTransition
	    }catch(Exception e_handleCmdDone){  
	    	 println( getName() + " plan=handleCmdDone WARNING:" + e_handleCmdDone.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleCmdDone
	    
	    StateFun backToHome = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("backToHome",-1);
	    	String myselfName = "backToHome";  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?curPos(0,0,D)" )) != null ){
	    	{//actionseq
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"robotCmd(M)","robotCmd(blinkStop)", guardVars ).toString();
	    	sendMsg("robotCmd","robot_adapter", QActorContext.dispatch, temporaryStr ); 
	    	temporaryStr = "\"AT HOME\"";
	    	println( temporaryStr );  
	    	it.unibo.planning.planUtil.showMap( myself  );
	    	it.unibo.utils.updateStateOnConsole.updateMap( myself  );
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?homeReady" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmdGoHome","cmdGoHome", guardVars ).toString();
	    	sendMsg("cmdGoHome",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	}
	    	else{ temporaryStr = QActorUtils.unifyMsgContent(pengine,"endAction","endAction", guardVars ).toString();
	    	sendMsg("endAction",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	}};//actionseq
	    	}
	    	else{ {//actionseq
	    	it.unibo.planning.planUtil.setGoal( myself ,"0", "0"  );
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?curPos(X,Y,D)" )) != null ){
	    	temporaryStr = "backToHome(X,Y,D)";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	it.unibo.planning.planUtil.doPlan( myself  );
	    	};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("terminate"), stateTab.get("home"), stateTab.get("home") }, 
	          new String[]{"true","M","endAction", "true","M","cmdGoHome", "true","M","robotHomeAfterBomb" },
	          100, "doActions" );//msgTransition
	    }catch(Exception e_backToHome){  
	    	 println( getName() + " plan=backToHome WARNING:" + e_backToHome.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//backToHome
	    
	    StateFun terminate = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("terminate",-1);
	    	String myselfName = "terminate";  
	    	temporaryStr = "\"STATE[terminate] retriever home!\"";
	    	println( temporaryStr );  
	    	it.unibo.utils.updateStateOnConsole.updateRobotState( myself ,"terminating"  );
	    	repeatPlanNoTransition(pr,myselfName,"robot_retriever_mind_"+myselfName,false,false);
	    }catch(Exception e_terminate){  
	    	 println( getName() + " plan=terminate WARNING:" + e_terminate.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//terminate
	    
	    StateFun handleError = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleError",-1);
	    	String myselfName = "handleError";  
	    	temporaryStr = "\"mind ERROR\"";
	    	println( temporaryStr );  
	    	repeatPlanNoTransition(pr,myselfName,"robot_retriever_mind_"+myselfName,false,false);
	    }catch(Exception e_handleError){  
	    	 println( getName() + " plan=handleError WARNING:" + e_handleError.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleError
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
