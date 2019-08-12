/* Generated by AN DISI Unibo */ 
package it.unibo.console;
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
public abstract class AbstractConsole extends QActor { 
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
		public AbstractConsole(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/console/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/console/plans.txt";
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
	    	stateTab.put("adaptCommand",adaptCommand);
	    	stateTab.put("handlePhoto",handlePhoto);
	    	stateTab.put("handleBagStatus",handleBagStatus);
	    	stateTab.put("handleAlert",handleAlert);
	    	stateTab.put("updateView",updateView);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "console tout : stops");  
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
	    	temporaryStr = "\"Console init\"";
	    	println( temporaryStr );  
	    	//bbb
	     msgTransition( pr,myselfName,"console_"+myselfName,false,
	          new StateFun[]{}, 
	          new String[]{},
	          100, "doWork" );//msgTransition
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun doWork = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_doWork",0);
	     pr.incNumIter(); 	
	    	String myselfName = "doWork";  
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(1000,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "doWork";
	    	if( ! aar.getGoon() ) return ;
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "temperature(X)","temperature(18)", guardVars ).toString();
	    	emit( "temperature", temporaryStr );
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(3000,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "doWork";
	    	if( ! aar.getGoon() ) return ;
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmdExplore","cmdExplore", guardVars ).toString();
	    	sendMsg("cmdExplore","robot_discovery_mind", QActorContext.dispatch, temporaryStr ); 
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(2000,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "doWork";
	    	if( ! aar.getGoon() ) return ;
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmdStop","cmdStop", guardVars ).toString();
	    	sendMsg("cmdStop","robot_discovery_mind", QActorContext.dispatch, temporaryStr ); 
	    	//bbb
	     msgTransition( pr,myselfName,"console_"+myselfName,false,
	          new StateFun[]{stateTab.get("adaptCommand"), stateTab.get("updateView"), stateTab.get("handlePhoto") }, 
	          new String[]{"true","E","usercmd", "true","E","robotState", "true","M","bag" },
	          60000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_doWork){  
	    	 println( getName() + " plan=doWork WARNING:" + e_doWork.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//doWork
	    
	    StateFun adaptCommand = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("adaptCommand",-1);
	    	String myselfName = "adaptCommand";  
	    	printCurrentEvent(false);
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(cmd(explore)))");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="cmdExplore";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(robotgui(cmd(explore)))"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("cmdExplore","robot_discovery_mind", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(cmd(halt)))");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="cmdStop";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(robotgui(cmd(halt)))"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("cmdStop","robot_discovery_mind", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(cmd(home)))");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="cmdGoHome";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(robotgui(cmd(home)))"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("cmdGoHome","robot_discovery_mind", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(bagStatus(bomb)))");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="bagStatus(bomb,args(picture(nothing)))";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(robotgui(bagStatus(bomb)))"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("bagStatus",getNameNoCtrl(), QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(bagStatus(bag)))");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="bagStatus(bag,args(nothing))";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("usercmd(X)"),  Term.createTerm("usercmd(robotgui(bagStatus(bag)))"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("bagStatus",getNameNoCtrl(), QActorContext.dispatch, parg ); 
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"console_"+myselfName,false,true);
	    }catch(Exception e_adaptCommand){  
	    	 println( getName() + " plan=adaptCommand WARNING:" + e_adaptCommand.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//adaptCommand
	    
	    StateFun handlePhoto = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_handlePhoto",0);
	     pr.incNumIter(); 	
	    	String myselfName = "handlePhoto";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("bag(picture(X))");
	    	if( currentMessage != null && currentMessage.msgId().equals("bag") && 
	    		pengine.unify(curT, Term.createTerm("bag(picture(X))")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		printCurrentMessage(false);
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"console_"+myselfName,false,
	          new StateFun[]{stateTab.get("handleBagStatus"), stateTab.get("adaptCommand") }, 
	          new String[]{"true","M","bagStatus", "true","E","usercmd" },
	          3000, "handlePhoto" );//msgTransition
	    }catch(Exception e_handlePhoto){  
	    	 println( getName() + " plan=handlePhoto WARNING:" + e_handlePhoto.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handlePhoto
	    
	    StateFun handleBagStatus = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleBagStatus",-1);
	    	String myselfName = "handleBagStatus";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("bagStatus(bomb,args(picture(X)))");
	    	if( currentMessage != null && currentMessage.msgId().equals("bagStatus") && 
	    		pengine.unify(curT, Term.createTerm("bagStatus(X,args(Y))")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine,"cmdGoHome","cmdGoHome", guardVars ).toString();
	    		sendMsg("cmdGoHome","robot_discovery_mind", QActorContext.dispatch, temporaryStr ); 
	    		temporaryStr = QActorUtils.unifyMsgContent(pengine,"alert","alert", guardVars ).toString();
	    		sendMsg("alert",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    		};//actionseq
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("bagStatus(bag,Y)");
	    	if( currentMessage != null && currentMessage.msgId().equals("bagStatus") && 
	    		pengine.unify(curT, Term.createTerm("bagStatus(X,args(Y))")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="cmdExplore";
	    		/* SendDispatch */
	    		parg = updateVars(Term.createTerm("bagStatus(X,args(Y))"),  Term.createTerm("bagStatus(bag,Y)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) sendMsg("cmdExplore","robot_discovery_mind", QActorContext.dispatch, parg ); 
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"console_"+myselfName,false,
	          new StateFun[]{stateTab.get("handleAlert") }, 
	          new String[]{"true","M","alert" },
	          3000, "doWork" );//msgTransition
	    }catch(Exception e_handleBagStatus){  
	    	 println( getName() + " plan=handleBagStatus WARNING:" + e_handleBagStatus.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleBagStatus
	    
	    StateFun handleAlert = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleAlert",-1);
	    	String myselfName = "handleAlert";  
	    	//bbb
	     msgTransition( pr,myselfName,"console_"+myselfName,false,
	          new StateFun[]{() -> {	//AD HOC state to execute an action and resumeLastPlan
	          try{
	            PlanRepeat pr1 = PlanRepeat.setUp("adhocstate",-1);
	            //ActionSwitch for a message or event
	             if( currentMessage.msgContent().startsWith("robotHome") ){
	            	String parg="cmdReachBomb";
	            	/* SendDispatch */
	            	parg = updateVars(Term.createTerm("robotHome"),  Term.createTerm("robotHome"), 
	            		    		  					Term.createTerm(currentMessage.msgContent()), parg);
	            	if( parg != null ) sendMsg("cmdReachBomb","robot_retriever_mind", QActorContext.dispatch, parg ); 
	             }
	            repeatPlanNoTransition(pr1,"adhocstate","adhocstate",false,true);
	          }catch(Exception e ){  
	             println( getName() + " plan=handleAlert WARNING:" + e.getMessage() );
	             //QActorContext.terminateQActorSystem(this); 
	          }
	          }
	          }, 
	          new String[]{"true","M","robotHome" },
	          3000, "handleAlert" );//msgTransition
	    }catch(Exception e_handleAlert){  
	    	 println( getName() + " plan=handleAlert WARNING:" + e_handleAlert.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleAlert
	    
	    StateFun updateView = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("updateView",-1);
	    	String myselfName = "updateView";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("state(X)");
	    	if( currentMessage != null && currentMessage.msgId().equals("robotState") && 
	    		pengine.unify(curT, Term.createTerm("state(position(X,Y),movement(M))")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		printCurrentMessage(false);
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"console_"+myselfName,false,true);
	    }catch(Exception e_updateView){  
	    	 println( getName() + " plan=updateView WARNING:" + e_updateView.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//updateView
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}