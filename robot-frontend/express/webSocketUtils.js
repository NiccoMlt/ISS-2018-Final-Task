const WebSocket = require('ws');
const wssPort = 3000;
const wss = new WebSocket.Server({ port: wssPort });

var mqtt = require('./mqttUtils');
var qaUtils = require('./qaUtils');

//create an array to hold your connections
var connections = [];

wss.on('connection', function (ws) {
  connections.push(ws);
  console.log('EXPRESS - WS opened from client');
  ws.on('message', function (message) {
      console.log('EXPRESS - WS received message');
      msg = qaUtils.QAmessageBuild("frontendUserCmd", message);
      mqtt.publishCommand(msg);
  });
  ws.on('close', function (reasonCode, description) {
    connections.splice(connections.indexOf(ws), 1);
    console.log((new Date()) + ' Peer ' + ws.remoteAddress + ' disconnected.');
  });
});

exports.port = wssPort;
exports.sendall = function(msg){
  //send the message to all of the
  //connections in the connection array
  console.log('EXPRESS - WS sending message');
  for (var i = 0; i < connections.length; i++) {
    connections[i].send(msg);
  }
}
