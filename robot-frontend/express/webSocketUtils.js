const WebSocket = require('ws');
const wss = new WebSocket.Server({ port: 3000 });

var mqtt = require('./mqttUtils');
var qaUtils = require('./qaUtils');

var webs;

wss.on('connection', function (ws) {
  webs = ws;
  console.log('EXPRESS - socket opened from client');
  ws.on('message', function (message) {
      console.log('EXPRESS - received: ' + message);
      msg = qaUtils.QAmessageBuild("frontendUserCmd", message);
      console.log("EXPRESS - MQTT emitting: " + msg);
      mqtt.publishCommand(msg);
  });
});

exports.send = function(msg){
    if (webs != undefined) {
      console.log('EXPRESS - webSocket send');
        webs.send(msg);
    }
}
