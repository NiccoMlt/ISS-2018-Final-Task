/* Generated by AN DISI Unibo */ 
package it.unibo.robot_discovery_mind;
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
public abstract class AbstractRobot_discovery_mind extends QActor { 
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
		public AbstractRobot_discovery_mind(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/robot_discovery_mind/WorldTheory.pl",
			setTheEnv( outEnvView )  , "home");
			this.planFilePath = "./srcMore/it/unibo/robot_discovery_mind/plans.txt";
	  	}
		@Override
		protected void doJob() throws Exception {
			String name  = getName().replace("_ctrl", "");
			mysupport = (IMsgQueue) QActorUtils.getQActor( name ); 
			initStateTable(); 
	 		initSensorSystem();
	 		history.push(stateTab.get( "home" ));
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
	    	stateTab.put("home",home);
	    	stateTab.put("goToExploration",goToExploration);
	    	stateTab.put("exploration",exploration);
	    	stateTab.put("goToHandleBag",goToHandleBag);
	    	stateTab.put("handleBag",handleBag);
	    	stateTab.put("goToIdle",goToIdle);
	    	stateTab.put("idle",idle);
	    	stateTab.put("returnHome",returnHome);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "robot_discovery_mind tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun home = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_home",0);
	     pr.incNumIter(); 	
	    	String myselfName = "home";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("robotHome");
	    	if( currentMessage != null && currentMessage.msgId().equals("robotHome") && 
	    		pengine.unify(curT, Term.createTerm("robotHome")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="robotHome";
	    		/* SendDispatch */
	    		parg = updateVars(Term.createTerm("robotHome"),  Term.createTerm("robotHome"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) sendMsg("robotHome","console", QActorContext.dispatch, parg ); 
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"robot_discovery_mind_"+myselfName,false,
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
	           stateTab.get("goToExploration") }, 
	          new String[]{"true","E","environment", " !?environment(ok)" ,"M","cmdExplore" },
	          60000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_home){  
	    	 println( getName() + " plan=home WARNING:" + e_home.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//home
	    
	    StateFun goToExploration = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("goToExploration",-1);
	    	String myselfName = "goToExploration";  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmd(X)","cmd(blinkStart)", guardVars ).toString();
	    	sendMsg("robotCmdPriority","robot_adapter", QActorContext.dispatch, temporaryStr ); 
	    	{
	    	String tStr1 = "ledState(off)";
	    	String tStr2 = "ledState(blinking)";
	    	 replaceRule( tStr1, tStr2 );  
	    	 }
	    	//bbb
	     msgTransition( pr,myselfName,"robot_discovery_mind_"+myselfName,false,
	          new StateFun[]{}, 
	          new String[]{},
	          100, "exploration" );//msgTransition
	    }catch(Exception e_goToExploration){  
	    	 println( getName() + " plan=goToExploration WARNING:" + e_goToExploration.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//goToExploration
	    
	    StateFun exploration = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("exploration",-1);
	    	String myselfName = "exploration";  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "cmd(X)","cmd(w)", guardVars ).toString();
	    	emit( "robotCmd", temporaryStr );
	    	temporaryStr = "\"EXPLORING ...\"";
	    	println( temporaryStr );  
	    	//bbb
	     msgTransition( pr,myselfName,"robot_discovery_mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("goToIdle"), stateTab.get("goToHandleBag") }, 
	          new String[]{"true","M","cmdStop", "true","M","robotNearBag" },
	          60000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_exploration){  
	    	 println( getName() + " plan=exploration WARNING:" + e_exploration.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//exploration
	    
	    StateFun goToHandleBag = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("goToHandleBag",-1);
	    	String myselfName = "goToHandleBag";  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"handleBag(X)","handleBag(halt)", guardVars ).toString();
	    	sendMsg("handleBag",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"handleBag(X)","handleBag(takePhoto)", guardVars ).toString();
	    	sendMsg("handleBag",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	//bbb
	     msgTransition( pr,myselfName,"robot_discovery_mind_"+myselfName,false,
	          new StateFun[]{}, 
	          new String[]{},
	          10, "handleBag" );//msgTransition
	    }catch(Exception e_goToHandleBag){  
	    	 println( getName() + " plan=goToHandleBag WARNING:" + e_goToHandleBag.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//goToHandleBag
	    
	    StateFun handleBag = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleBag",-1);
	    	String myselfName = "handleBag";  
	    	temporaryStr = "\"HANDLING BAG ...\"";
	    	println( temporaryStr );  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("handleBag(halt)");
	    	if( currentMessage != null && currentMessage.msgId().equals("handleBag") && 
	    		pengine.unify(curT, Term.createTerm("handleBag(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine, "cmd(X)","cmd(h)", guardVars ).toString();
	    		emit( "robotCmd", temporaryStr );
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine, "cmd(X)","cmd(blinkStop)", guardVars ).toString();
	    		emit( "robotCmd", temporaryStr );
	    		{
	    		String tStr1 = "ledState(blinking)";
	    		String tStr2 = "ledState(off)";
	    		 replaceRule( tStr1, tStr2 );  
	    		 }
	    		};//actionseq
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("handleBag(takePhoto)");
	    	if( currentMessage != null && currentMessage.msgId().equals("handleBag") && 
	    		pengine.unify(curT, Term.createTerm("handleBag(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		temporaryStr = "\"TAKE PHOTO ...\"";
	    		println( temporaryStr );  
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine,"bag(picture(X))","bag(picture(X))", guardVars ).toString();
	    		sendMsg("bag","console", QActorContext.dispatch, temporaryStr ); 
	    		};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"robot_discovery_mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("handleBag"), stateTab.get("returnHome"), stateTab.get("goToExploration") }, 
	          new String[]{"true","M","handleBag", "true","M","cmdGoHome", "true","M","cmdExplore" },
	          1000, "handleBag" );//msgTransition
	    }catch(Exception e_handleBag){  
	    	 println( getName() + " plan=handleBag WARNING:" + e_handleBag.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleBag
	    
	    StateFun goToIdle = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("goToIdle",-1);
	    	String myselfName = "goToIdle";  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmd(X)","cmd(h)", guardVars ).toString();
	    	sendMsg("robotCmdPriority","robot_adapter", QActorContext.dispatch, temporaryStr ); 
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmd(X)","cmd(blinkStop)", guardVars ).toString();
	    	sendMsg("robotCmdPriority","robot_adapter", QActorContext.dispatch, temporaryStr ); 
	    	{
	    	String tStr1 = "ledState(blinking)";
	    	String tStr2 = "ledState(off)";
	    	 replaceRule( tStr1, tStr2 );  
	    	 }
	    	//bbb
	     msgTransition( pr,myselfName,"robot_discovery_mind_"+myselfName,false,
	          new StateFun[]{}, 
	          new String[]{},
	          100, "idle" );//msgTransition
	    }catch(Exception e_goToIdle){  
	    	 println( getName() + " plan=goToIdle WARNING:" + e_goToIdle.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//goToIdle
	    
	    StateFun idle = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("idle",-1);
	    	String myselfName = "idle";  
	    	//bbb
	     msgTransition( pr,myselfName,"robot_discovery_mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("exploration"), stateTab.get("returnHome") }, 
	          new String[]{"true","M","cmdExplore", "true","M","cmdGoHome" },
	          60000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_idle){  
	    	 println( getName() + " plan=idle WARNING:" + e_idle.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//idle
	    
	    StateFun returnHome = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("returnHome",-1);
	    	String myselfName = "returnHome";  
	    	temporaryStr = "\"GOING HOME...\"";
	    	println( temporaryStr );  
	    	//bbb
	     msgTransition( pr,myselfName,"robot_discovery_mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("home") }, 
	          new String[]{"true","M","robotHome" },
	          60000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_returnHome){  
	    	 println( getName() + " plan=returnHome WARNING:" + e_returnHome.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//returnHome
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
