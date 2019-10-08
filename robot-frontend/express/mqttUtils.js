const mqtt = require ('mqtt');
const topicPublisher = "unibo/frontendUserCmd";
const topicSubscriber = "unibo/frontendRobotState";

var client   = mqtt.connect('mqtt://broker.hivemq.com');

var webSocket = require('./webSocketUtils');
var qaUtils = require('./qaUtils');

client.on('connect', function () {
	  client.subscribe( topicSubscriber );
	  console.log('EXPRESS - MQTT client has subscribed successfully ');
});

//The message usually arrives as buffer, so I had to convert it to string data type;
client.on('message', function (topic, message){
	var msg = message.toString();
  console.log("EXPRESS - MQTT RECEIVES:"+ msg);
	state = qaUtils.parseQAmessage(msg);
	console.log(state);
	webSocket.send(state);
});

exports.publish = function( msg ){
  console.log('EXPRESS - mqtt publish ' + client);
	client.publish(topicPublisher, msg);
}
