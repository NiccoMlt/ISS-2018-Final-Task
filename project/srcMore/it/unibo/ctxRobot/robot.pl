%====================================================================================
% Context ctxRobot  SYSTEM-configuration: file it.unibo.ctxRobot.robot.pl 
%====================================================================================
pubsubserveraddr("").
pubsubsystopic("unibo/qasys").
%%% -------------------------------------------
context(ctxrobot, "localhost",  "TCP", "8079" ).  		 
%%% -------------------------------------------
qactor( world_observer , ctxrobot, "it.unibo.world_observer.MsgHandle_World_observer"   ). %%store msgs 
qactor( world_observer_ctrl , ctxrobot, "it.unibo.world_observer.World_observer"   ). %%control-driven 
qactor( robot_adapter , ctxrobot, "it.unibo.robot_adapter.MsgHandle_Robot_adapter"   ). %%store msgs 
qactor( robot_adapter_ctrl , ctxrobot, "it.unibo.robot_adapter.Robot_adapter"   ). %%control-driven 
qactor( onecellforward , ctxrobot, "it.unibo.onecellforward.MsgHandle_Onecellforward"   ). %%store msgs 
qactor( onecellforward_ctrl , ctxrobot, "it.unibo.onecellforward.Onecellforward"   ). %%control-driven 
qactor( robot_advanced , ctxrobot, "it.unibo.robot_advanced.MsgHandle_Robot_advanced"   ). %%store msgs 
qactor( robot_advanced_ctrl , ctxrobot, "it.unibo.robot_advanced.Robot_advanced"   ). %%control-driven 
qactor( robot_discovery_mind , ctxrobot, "it.unibo.robot_discovery_mind.MsgHandle_Robot_discovery_mind"   ). %%store msgs 
qactor( robot_discovery_mind_ctrl , ctxrobot, "it.unibo.robot_discovery_mind.Robot_discovery_mind"   ). %%control-driven 
qactor( robot_retriever_mind , ctxrobot, "it.unibo.robot_retriever_mind.MsgHandle_Robot_retriever_mind"   ). %%store msgs 
qactor( robot_retriever_mind_ctrl , ctxrobot, "it.unibo.robot_retriever_mind.Robot_retriever_mind"   ). %%control-driven 
qactor( console , ctxrobot, "it.unibo.console.MsgHandle_Console"   ). %%store msgs 
qactor( console_ctrl , ctxrobot, "it.unibo.console.Console"   ). %%control-driven 
%%% -------------------------------------------
eventhandler(collisionevh,ctxrobot,"it.unibo.ctxRobot.Collisionevh","robotSonarObstacle").  
%%% -------------------------------------------

