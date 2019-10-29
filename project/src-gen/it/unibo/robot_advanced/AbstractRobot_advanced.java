/* Generated by AN DISI Unibo */ 
package it.unibo.robot_advanced;
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
public abstract class AbstractRobot_advanced extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	//protected String mqttServer = "tcp://127.0.0.1:1883";
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractRobot_advanced(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/robot_advanced/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/robot_advanced/plans.txt";
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
	    	stateTab.put("doWork",doWork);
	    	stateTab.put("executeCommand",executeCommand);
	    	stateTab.put("waitMoveComletedAnswer",waitMoveComletedAnswer);
	    	stateTab.put("receivedMoveCompletedAnswer",receivedMoveCompletedAnswer);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "robot_advanced tout : stops");  
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
	    	//switchTo doWork
	        switchToPlanAsNextState(pr, myselfName, "robot_advanced_"+myselfName, 
	              "doWork",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun doWork = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("doWork",-1);
	    	String myselfName = "doWork";  
	    	//bbb
	     msgTransition( pr,myselfName,"robot_advanced_"+myselfName,false,
	          new StateFun[]{stateTab.get("executeCommand") }, 
	          new String[]{"true","M","robotCmd" },
	          60000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_doWork){  
	    	 println( getName() + " plan=doWork WARNING:" + e_doWork.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//doWork
	    
	    StateFun executeCommand = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("executeCommand",-1);
	    	String myselfName = "executeCommand";  
	    	storeCurrentMessageForReply();
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("robotCmd(a)");
	    	if( currentMessage != null && currentMessage.msgId().equals("robotCmd") && 
	    		pengine.unify(curT, Term.createTerm("robotCmd(M)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		if( (guardVars = QActorUtils.evalTheGuard(this, " !?timeTurn(T)" )) != null ){
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine,"robotCmd(M,T)","robotCmd(a,T)", guardVars ).toString();
	    		sendMsg("robotAdapterCmd","robot_adapter", QActorContext.dispatch, temporaryStr ); 
	    		}
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine,"waitMoveCompleted","waitMoveCompleted", guardVars ).toString();
	    		sendMsg("waitMoveCompleted",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    		};//actionseq
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("robotCmd(d)");
	    	if( currentMessage != null && currentMessage.msgId().equals("robotCmd") && 
	    		pengine.unify(curT, Term.createTerm("robotCmd(M)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		if( (guardVars = QActorUtils.evalTheGuard(this, " !?timeTurn(T)" )) != null ){
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine,"robotCmd(M,T)","robotCmd(d,T)", guardVars ).toString();
	    		sendMsg("robotAdapterCmd","robot_adapter", QActorContext.dispatch, temporaryStr ); 
	    		}
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine,"waitMoveCompleted","waitMoveCompleted", guardVars ).toString();
	    		sendMsg("waitMoveCompleted",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    		};//actionseq
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("robotCmd(w)");
	    	if( currentMessage != null && currentMessage.msgId().equals("robotCmd") && 
	    		pengine.unify(curT, Term.createTerm("robotCmd(M)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		if( (guardVars = QActorUtils.evalTheGuard(this, " !?timew(T)" )) != null ){
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine,"moveMsgCmd(TF)","moveMsgCmd(T)", guardVars ).toString();
	    		sendMsg("moveMsgCmd","onecellforward", QActorContext.dispatch, temporaryStr ); 
	    		}
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine,"waitMoveCompleted","waitMoveCompleted", guardVars ).toString();
	    		sendMsg("waitMoveCompleted",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    		};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"robot_advanced_"+myselfName,false,
	          new StateFun[]{stateTab.get("waitMoveComletedAnswer") }, 
	          new String[]{"true","M","waitMoveCompleted" },
	          100, "doWork" );//msgTransition
	    }catch(Exception e_executeCommand){  
	    	 println( getName() + " plan=executeCommand WARNING:" + e_executeCommand.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//executeCommand
	    
	    StateFun waitMoveComletedAnswer = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("waitMoveComletedAnswer",-1);
	    	String myselfName = "waitMoveComletedAnswer";  
	    	//bbb
	     msgTransition( pr,myselfName,"robot_advanced_"+myselfName,false,
	          new StateFun[]{stateTab.get("receivedMoveCompletedAnswer"), stateTab.get("receivedMoveCompletedAnswer") }, 
	          new String[]{"true","M","moveMsgCmdDone", "true","M","moveMsgCmdObstacle" },
	          60000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_waitMoveComletedAnswer){  
	    	 println( getName() + " plan=waitMoveComletedAnswer WARNING:" + e_waitMoveComletedAnswer.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//waitMoveComletedAnswer
	    
	    StateFun receivedMoveCompletedAnswer = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("receivedMoveCompletedAnswer",-1);
	    	String myselfName = "receivedMoveCompletedAnswer";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("moveMsgCmdDone(X)");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveMsgCmdDone") && 
	    		pengine.unify(curT, Term.createTerm("moveMsgCmdDone(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="moveMsgCmdDone(X)";
	    		/* ReplyToCaller */
	    		parg = updateVars( Term.createTerm("moveMsgCmdDone(X)"),  Term.createTerm("moveMsgCmdDone(X)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) replyToCaller("moveMsgCmdDone", parg);
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("moveMsgCmdObstacle(moveWDuration(T))");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveMsgCmdObstacle") && 
	    		pengine.unify(curT, Term.createTerm("moveMsgCmdObstacle(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="robotCmd(s,T)";
	    		/* SendDispatch */
	    		parg = updateVars(Term.createTerm("moveMsgCmdObstacle(X)"),  Term.createTerm("moveMsgCmdObstacle(moveWDuration(T))"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) sendMsg("robotAdapterCmd","robot_adapter", QActorContext.dispatch, parg ); 
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("moveMsgCmdObstacle(moveWDuration(T))");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveMsgCmdObstacle") && 
	    		pengine.unify(curT, Term.createTerm("moveMsgCmdObstacle(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="moveMsgCmdObstacle(moveWDuration(T))";
	    		/* ReplyToCaller */
	    		parg = updateVars( Term.createTerm("moveMsgCmdObstacle(X)"),  Term.createTerm("moveMsgCmdObstacle(moveWDuration(T))"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) replyToCaller("moveMsgCmdObstacle", parg);
	    	}
	    	//switchTo doWork
	        switchToPlanAsNextState(pr, myselfName, "robot_advanced_"+myselfName, 
	              "doWork",false, false, null); 
	    }catch(Exception e_receivedMoveCompletedAnswer){  
	    	 println( getName() + " plan=receivedMoveCompletedAnswer WARNING:" + e_receivedMoveCompletedAnswer.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//receivedMoveCompletedAnswer
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
