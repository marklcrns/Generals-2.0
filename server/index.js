const app = require('express')();
const http = require('http').createServer(app);
const io = require('socket.io')(http);
const port = 8080;

let firstMoveMaker = null;
let hostPlayerId = null;
let players = [];
let moveHistory = [];

http.listen(port, function() {
    console.log("Server running. Listening on port " + port + "...");
});

io.on('connection', (socket) => {
    console.log("Player Connected!");
    socket.emit('socketID', { id: socket.id });

    if (firstMoveMaker == null) {
        console.log("Setting up game host...");
        hostPlayerId = socket.id;
        socket.emit('setupGame', { 'isClientHost': 'true' });
    } else {
        console.log("Connecting to game host...");
        socket.emit('setupGame', {
            'isClientHost': 'false',
            'takenAlliance': players[0].alliance,
            'firstMoveMaker': firstMoveMaker });
    }

    socket.on('startGame', (data) => {
        players.push(new player(socket.id, data.myAlliance));
        firstMoveMaker = data.firstMoveMaker;
    });

    socket.broadcast.emit('newPlayer', { id: socket.id });

    socket.on('makeTurnMove', (data) => {
        data.id = socket.id
        moveHistory.push(new move(socket.id, data.turnId, data.srcTileId, data.destTileId));
        console.log(moveHistory.length);
        socket.broadcast.emit('makeTurnMove', data);
    });

    socket.on('disconnect', () => {
        if (socket.id == hostPlayerId) {
            firstMoveMaker = null
            hostPlayerId = null;
            players = [];
            moveHistory = []
            console.log("Host player disconnected");
        }
        console.log("Disconnecting...");
    });

});

function player(id, alliance) {
    this.id = id;
    this.alliance = alliance;
}

function move(playerId, turnId, srcTileId, destTileId) {
    this.turnId = turnId;
    this.srcTileId = srcTileId;
    this.destTileId = destTileId;
}
