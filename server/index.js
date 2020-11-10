const app = require('express')();
const http = require('http').createServer(app);
const io = require('socket.io')(http);
const port = 8080;

// Pre-game variables
let firstMoveMakerAlliance = null;
let hostPlayerId = null;
let players = [];

// In-game variables
let isGameRunning = false;
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
    players.push(new player(socket.id, null));

    if (hostPlayerId == null) {
        console.log("Setting up game host...");
        hostPlayerId = socket.id;
        socket.emit('setupGame', {
            isClientHost: true
        });
    } else {
        socket.emit('getPlayers', { players: players });
        console.log("Connecting to game host...");
        socket.emit('setupGame', {
            isClientHost: false,
        });
    }

    socket.on('playerReady', (data) => {
        console.log("players length: " + players.length)

        // Start game
        if (data.isReady && players.length >= 2) {
            startGame();

            io.sockets.emit('playersHandshake', {
                players: players,
                firstMoveMaker: firstMoveMakerAlliance
            })

            console.log("Host Alliance: " + players[0].alliance);
            console.log("First Move: " + firstMoveMakerAlliance);
        }
    });

    socket.on('makeTurnMove', (data) => {
        // Validate turn ID and move maker then broadcast move
        if (isGameRunning && data.turnId == currentTurn &&
            currentMoveMaker == data.socketId) {
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
    assignAllianceRandomly();
    randomFirstMove();
    currentTurn = 1;
    isGameRunning = true;
}

function assignAllianceRandomly() {
    if (Math.random() < 0.5) {
        players[0].alliance = "WHITE";
        players[1].alliance = "BLACK";
    } else {
        players[1].alliance = "WHITE";
        players[0].alliance = "BLACK";
    }
}

function randomFirstMove() {
    if (Math.random() < 0.5) {
        firstMoveMakerAlliance = players[0].alliance;
        currentMoveMaker = players[0].id;
    } else {
        firstMoveMakerAlliance = players[1].alliance;
        currentMoveMaker = players[1].id;
    }
}

function switchMoveMaker() {
    if (currentMoveMaker == players[0].id)
        currentMoveMaker = players[1].id;
    else
        currentMoveMaker = players[0].id;
}

function resetGame() {
    firstMoveMakerAlliance = null
    hostPlayerId = null;
    players = [];
    moveHistory = []
    isGameRunning = false;
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

