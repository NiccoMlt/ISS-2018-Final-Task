const WebSocket = require('ws');
const wssPort = 3000;
const wss = new WebSocket.Server({ port: wssPort });

var mqtt = require('./mqttUtils');
var qaUtils = require('./qaUtils');

var webs;

wss.on('connection', function (ws) {
  webs = ws;
  console.log('EXPRESS - WS opened from client');
  ws.on('message', function (message) {
      console.log('EXPRESS - WS received message');
      msg = qaUtils.QAmessageBuild("frontendUserCmd", message);
      mqtt.publishCommand(msg);
  });
});

exports.port = wssPort;
exports.send = function(msg){
    if (webs != undefined) {
      console.log('EXPRESS - WS sending message');
      webs.send(msg);
    }
}
