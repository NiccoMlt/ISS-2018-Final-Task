%====================================================================================
% Context ctxRobot  SYSTEM-configuration: file it.unibo.ctxRobot.robot.pl 
%====================================================================================
pubsubserveraddr("").
pubsubsystopic("unibo/qasys").
%%% -------------------------------------------
context(ctxrobot, "localhost",  "TCP", "8079" ).  		 
%%% -------------------------------------------
qactor( worldobserver , ctxrobot, "it.unibo.worldobserver.MsgHandle_Worldobserver"   ). %%store msgs 
qactor( worldobserver_ctrl , ctxrobot, "it.unibo.worldobserver.Worldobserver"   ). %%control-driven 
qactor( robotadapter , ctxrobot, "it.unibo.robotadapter.MsgHandle_Robotadapter"   ). %%store msgs 
qactor( robotadapter_ctrl , ctxrobot, "it.unibo.robotadapter.Robotadapter"   ). %%control-driven 
qactor( robotposition , ctxrobot, "it.unibo.robotposition.MsgHandle_Robotposition"   ). %%store msgs 
qactor( robotposition_ctrl , ctxrobot, "it.unibo.robotposition.Robotposition"   ). %%control-driven 
qactor( mind , ctxrobot, "it.unibo.mind.MsgHandle_Mind"   ). %%store msgs 
qactor( mind_ctrl , ctxrobot, "it.unibo.mind.Mind"   ). %%control-driven 
qactor( console , ctxrobot, "it.unibo.console.MsgHandle_Console"   ). %%store msgs 
qactor( console_ctrl , ctxrobot, "it.unibo.console.Console"   ). %%control-driven 
%%% -------------------------------------------
%%% -------------------------------------------

