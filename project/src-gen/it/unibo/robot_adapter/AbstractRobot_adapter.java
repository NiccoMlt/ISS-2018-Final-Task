/* Generated by AN DISI Unibo */ 
package it.unibo.robot_adapter;
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
public abstract class AbstractRobot_adapter extends QActor { 
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
		public AbstractRobot_adapter(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/robot_adapter/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/robot_adapter/plans.txt";
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
	    	stateTab.put("initRobots",initRobots);
	    	stateTab.put("doWork",doWork);
	    	stateTab.put("executeCommand",executeCommand);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "robot_adapter tout : stops");  
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
	    	//bbb
	     msgTransition( pr,myselfName,"robot_adapter_"+myselfName,false,
	          new StateFun[]{}, 
	          new String[]{},
	          100, "initRobots" );//msgTransition
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun initRobots = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_initRobots",0);
	     pr.incNumIter(); 	
	    	String myselfName = "initRobots";  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " ??robotType(T,setuparg(A))" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"robot(T,A)","robot(T,A)", guardVars ).toString();
	    	sendMsg("robotAdapterAdd",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"robot_adapter_"+myselfName,false,
	          new StateFun[]{() -> {	//AD HOC state to execute an action and resumeLastPlan
	          try{
	            PlanRepeat pr1 = PlanRepeat.setUp("adhocstate",-1);
	            //ActionSwitch for a message or event
	             if( currentMessage.msgContent().startsWith("robot") ){
	            	{/* JavaLikeMove */ 
	            	String arg1 = "T" ;
	            	arg1 =  updateVars( Term.createTerm("robot(T,A)"), Term.createTerm("robot(T,A)"), 
	            		                Term.createTerm(currentMessage.msgContent()),  arg1 );	                
	            	//end arg1
	            	String arg2 = "A" ;
	            	arg2 =  updateVars( Term.createTerm("robot(T,A)"), Term.createTerm("robot(T,A)"), 
	            		                Term.createTerm(currentMessage.msgContent()),  arg2 );	                
	            	//end arg2
	            	it.unibo.robot_adapter.robots.setUp(this,arg1,arg2 );
	            	}
	             }
	            repeatPlanNoTransition(pr1,"adhocstate","adhocstate",false,true);
	          }catch(Exception e ){  
	             println( getName() + " plan=initRobots WARNING:" + e.getMessage() );
	             //QActorContext.terminateQActorSystem(this); 
	          }
	          }
	          }, 
	          new String[]{"true","M","robotAdapterAdd" },
	          2000, "doWork" );//msgTransition
	    }catch(Exception e_initRobots){  
	    	 println( getName() + " plan=initRobots WARNING:" + e_initRobots.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//initRobots
	    
	    StateFun doWork = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_doWork",0);
	     pr.incNumIter(); 	
	    	String myselfName = "doWork";  
	    	//bbb
	     msgTransition( pr,myselfName,"robot_adapter_"+myselfName,false,
	          new StateFun[]{stateTab.get("executeCommand"), stateTab.get("executeCommand") }, 
	          new String[]{"true","M","robotCmd", "true","M","robotAdapterCmd" },
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
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("robotCmd(X)");
	    	if( currentMessage != null && currentMessage.msgId().equals("robotCmd") && 
	    		pengine.unify(curT, Term.createTerm("robotCmd(M)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		{/* JavaLikeMove */ 
	    		String arg1 = "X" ;
	    		arg1 =  updateVars( Term.createTerm("robotCmd(M)"), Term.createTerm("robotCmd(X)"), 
	    			                Term.createTerm(currentMessage.msgContent()),  arg1 );	                
	    		//end arg1
	    		it.unibo.robot_adapter.robots.doMove(this,arg1 );
	    		}
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("robotCmd(X,T)");
	    	if( currentMessage != null && currentMessage.msgId().equals("robotAdapterCmd") && 
	    		pengine.unify(curT, Term.createTerm("robotCmd(M,T)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		{/* JavaLikeMove */ 
	    		String arg1 = "X" ;
	    		arg1 =  updateVars( Term.createTerm("robotCmd(M,T)"), Term.createTerm("robotCmd(X,T)"), 
	    			                Term.createTerm(currentMessage.msgContent()),  arg1 );	                
	    		//end arg1
	    		String arg2 = "T" ;
	    		arg2 =  updateVars( Term.createTerm("robotCmd(M,T)"), Term.createTerm("robotCmd(X,T)"), 
	    			                Term.createTerm(currentMessage.msgContent()),  arg2 );	                
	    		//end arg2
	    		it.unibo.robot_adapter.robots.doMove(this,arg1,arg2 );
	    		}
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"robot_adapter_"+myselfName,false,true);
	    }catch(Exception e_executeCommand){  
	    	 println( getName() + " plan=executeCommand WARNING:" + e_executeCommand.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//executeCommand
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
