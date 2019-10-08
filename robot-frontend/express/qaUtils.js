exports.parseQAmessage = function( msg ){
	start = msg.indexOf( "frontendRobotState(" )+19;
    end = msg.indexOf( ")," );
    return msg.substring(start,end);
}

exports.QAmessageBuild = function( cmd ){
    return "msg(frontendUserCmd,event,js,none,frontendUserCmd(" + cmd.substring(1,cmd.length-1) + "),1)"
}
