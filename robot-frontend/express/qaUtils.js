exports.parseQAmessage = function( msg ){
	start = msg.indexOf( "frontendRobotState(" )+19;
    end = msg.indexOf( ")," );
    return msg.substring(start,end);
}

exports.QAmessageBuild = function( id, cmd ){
    return "msg(" + id + ",event,js,none," + id + "(" + cmd.substring(1,cmd.length-1) + "),1)"
}
