const config = require("./config");

const express = require('express');
const http = require('http');
const path = require('path');

const app = express();

exports.express = express;
exports.http = http;
exports.path = path;
exports.app = app;

exports.config = function () {

  // Server side socket
  require('./mqttUtils');

  // CORS on express
  app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
  });

  // Environment service
  var mqtt = require('./mqttUtils');
  var qaUtils = require('./qaUtils');
  const bodyParser = require('body-parser');
  app.use(bodyParser.text());
  app.post('/environment/temperature', function (req, res) {
    console.log("Received post with value " + req.body);
    msg = qaUtils.QAmessageBuild("temperature", req.body);
    console.log("------------EXPRESS - MQTT emitting: " + msg);
    mqtt.publishEnvironment(msg);
    res.end();
  });
}

exports.launch = function(port = config.default_port) {
  const server = http.createServer(app);

  server.listen(port, () => {
    console.log('Running on port ' + port + ' ...');
    console.log('EXPRESS - Waiting for client to connect to web-socket ...');
  });
}
