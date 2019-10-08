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
	//protected String mqttServer = "tcp://broker.hivemq.com:1883";
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractRobot_retriever_mind(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/robot_retriever_mind/WorldTheory.pl",
			setTheEnv( outEnvView )  , "home");
			this.planFilePath = "./srcMore/it/unibo/robot_retriever_mind/plans.txt";
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
	    	stateTab.put("checkTemperatureAndRetrieve",checkTemperatureAndRetrieve);
	    	stateTab.put("goToReachBomb",goToReachBomb);
	    	stateTab.put("reachBomb",reachBomb);
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
	           stateTab.get("checkTemperatureAndRetrieve") }, 
	          new String[]{"true","E","environment", "true","M","cmdReachBomb" },
	          60000, "handleToutBuiltIn" );//msgTransition
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
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_mind_"+myselfName,false,
	          new StateFun[]{}, 
	          new String[]{},
	          1000, "reachBomb" );//msgTransition
	    }catch(Exception e_goToReachBomb){  
	    	 println( getName() + " plan=goToReachBomb WARNING:" + e_goToReachBomb.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//goToReachBomb
	    
	    StateFun reachBomb = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("reachBomb",-1);
	    	String myselfName = "reachBomb";  
	    	temporaryStr = "\"RETRIVING BOMB ...\"";
	    	println( temporaryStr );  
	    	//bbb
	     msgTransition( pr,myselfName,"robot_retriever_mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("home") }, 
	          new String[]{"true","M","robotHome" },
	          3000, "reachBomb" );//msgTransition
	    }catch(Exception e_reachBomb){  
	    	 println( getName() + " plan=reachBomb WARNING:" + e_reachBomb.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//reachBomb
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
