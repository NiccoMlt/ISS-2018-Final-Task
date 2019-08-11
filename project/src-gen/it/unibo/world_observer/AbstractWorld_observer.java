/* Generated by AN DISI Unibo */ 
package it.unibo.world_observer;
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
public abstract class AbstractWorld_observer extends QActor { 
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
		public AbstractWorld_observer(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/world_observer/WorldTheory.pl",
			setTheEnv( outEnvView )  , "doObserve");
			this.planFilePath = "./srcMore/it/unibo/world_observer/plans.txt";
	  	}
		@Override
		protected void doJob() throws Exception {
			String name  = getName().replace("_ctrl", "");
			mysupport = (IMsgQueue) QActorUtils.getQActor( name ); 
			initStateTable(); 
	 		initSensorSystem();
	 		history.push(stateTab.get( "doObserve" ));
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
	    	stateTab.put("doObserve",doObserve);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "world_observer tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun doObserve = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_doObserve",0);
	     pr.incNumIter(); 	
	    	String myselfName = "doObserve";  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?evaluateEnvironment" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "environment(X)","environment(ok)", guardVars ).toString();
	    	emit( "environment", temporaryStr );
	    	}
	    	else{ temporaryStr = QActorUtils.unifyMsgContent(pengine, "environment(X)","environment(notok)", guardVars ).toString();
	    	emit( "environment", temporaryStr );
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"world_observer_"+myselfName,false,
	          new StateFun[]{() -> {	//AD HOC state to execute an action and resumeLastPlan
	          try{
	            PlanRepeat pr1 = PlanRepeat.setUp("adhocstate",-1);
	            //ActionSwitch for a message or event
	             if( currentEvent.getMsg().startsWith("temperature") ){
	            	/* replaceRule */
	            	String parg1="temperature(Z)"; 
	            	String parg2="temperature(X)"; 
	            	parg1 = updateVars( Term.createTerm("temperature(X)"),  Term.createTerm("temperature(X)"), 
	            		    		  			Term.createTerm(currentEvent.getMsg()), parg1);
	            	parg2 = updateVars( Term.createTerm("temperature(X)"),  Term.createTerm("temperature(X)"), 
	            		    		  			Term.createTerm(currentEvent.getMsg()), parg2);
	            	replaceRule(parg1,parg2);
	             }
	            repeatPlanNoTransition(pr1,"adhocstate","adhocstate",false,true);
	          }catch(Exception e ){  
	             println( getName() + " plan=doObserve WARNING:" + e.getMessage() );
	             //QActorContext.terminateQActorSystem(this); 
	          }
	          }
	          }, 
	          new String[]{"true","E","temperature" },
	          1000, "doObserve" );//msgTransition
	    }catch(Exception e_doObserve){  
	    	 println( getName() + " plan=doObserve WARNING:" + e_doObserve.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//doObserve
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
