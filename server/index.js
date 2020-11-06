const app = require('express')();
const http = require('http').createServer(app);
const io = require('socket.io')(http);
const port = 8080;

// Pre-game variables
let firstMoveMakerAlliance = null;
let hostPlayerId = null;
let players = [];

// In-game variables
let currentTurn = 0;
let currentMoveMaker = null;
let moveHistory = [];

http.listen(port, function() {
    console.log("Server running. Listening on port " + port + "...");
});

io.on('connection', (socket) => {
    // Get all players socket IDs if existing
    // Broadcast all existing players of a new player connected
    // Set up game
        // configure host player
        // OR
        // configure new player
    // listen for players if ready
    // Player handshake
    // Startgame if both players connected

    socket.on('disconnect', () => {
        if (socket.id == hostPlayerId) {
            resetGame();
            console.log("Host player disconnected");
        } else {
            console.log("Player disconnected");
        }
    });

    socket.emit('socketId', { id: socket.id, });
    socket.broadcast.emit('newPlayer', { id: socket.id });

    if (players.length > 0) {
        socket.emit('getPlayers', { players: players });
    }

    if (hostPlayerId == null) {
        console.log("Setting up game host...");
        hostPlayerId = socket.id;
        socket.emit('setupGame', {
            isClientHost: true
        });
    } else {
        console.log("Connecting to game host...");
        socket.emit('setupGame', {
            isClientHost: false,
            takenAlliance: players[0].alliance,
        });
    }

    socket.on('playerReady', (data) => {
        myAlliance = data.myAlliance;
        players.push(new player(socket.id, data.myAlliance));

        console.log("players: " + players.length)

        // Start game
        if (players.length == 2) {
            startGame();
            socket.emit('playersHandshake', { firstMoveMaker: firstMoveMakerAlliance })
            socket.broadcast.emit('playersHandshake', { firstMoveMaker: firstMoveMakerAlliance })

            console.log("Host Alliance: " + players[0].alliance);
            console.log("First Move: " + firstMoveMakerAlliance);
        }
    });

    socket.on('makeTurnMove', (data) => {
        if (data.turnId == currentTurn) {
            let newMove = new move(data.socketId, data.turnId, data.srcTileId, data.tgtTileId)
            moveHistory.push(newMove);
            currentTurn++;
            switchMoveMaker();
            console.log(moveHistory.length);
            console.log("playerId: " + newMove.playerId + "; turnId: "+ newMove.turnId +
                        "; from: " + newMove.srcTileId + "; to: " + newMove.tgtTileId);
            socket.broadcast.emit('makeTurnMove', data);
        }
    });

});

function startGame() {
    randomFirstMove();
    currentTurn = 1;
    currentMoveMaker = firstMoveMakerAlliance;
}

function randomFirstMove() {
    if (Math.random() < 0.5)
        firstMoveMakerAlliance = "WHITE";
    else
        firstMoveMakerAlliance = "BLACK";
}

function switchMoveMaker() {
    if (currentMoveMaker == "WHITE")
        currentMoveMaker = "BLACK";
    else
        currentMoveMaker = "WHITE";
}

function resetGame() {
    firstMoveMakerAlliance = null
    hostPlayerId = null;
    players = [];
    moveHistory = []
    currentTurn = 0;
    currentMoveMaker = null;
}

function player(id, alliance) {
    this.id = id;
    this.alliance = alliance;
}

function move(playerId, turnId, srcTileId, tgtTileId) {
    this.playerId = playerId;
    this.turnId = turnId;
    this.srcTileId = srcTileId;
    this.tgtTileId = tgtTileId;
}

