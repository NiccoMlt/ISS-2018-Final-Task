const express = require('express');
const http = require('http');
const path = require('path');

// Server side socket
require('./express/index');

const app = express();

const port = process.env.PORT || 3001

// Angular renderer
app.use(express.static(__dirname + '/dist/robot-frontend'));

app.get('/*', (req,res) => res.sendFile(path.join(__dirname)));

const server = http.createServer(app);

server.listen(port, () => {
  console.log('Running on port ' + port + ' ...');
  console.log('EXPRESS - Waiting for client to connect to web-socket ...');
});
