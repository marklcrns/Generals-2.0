const app = require('express')();
const http = require('http').createServer(app);
const io = require('socket.io')(http);
const port = 8080;

// Pre-game variables
let firstMoveMakerAlliance = null;
let hostPlayerId = null;
let players = [];
let boardConfig = new Map();

// In-game variables
let isGameRunning = false;
let currentTurn = 0;
let currentMoveMaker = null;
let moveHistory = [];

http.listen(port, function() {
    console.log("Server running. Listening on port " + port + "...");
});

io.on('connection', (socket) => {

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
        players[0].alliance = "WHITE";
        // assignAllianceRandomly();

        console.log("Setting up game host...");

        hostPlayerId = socket.id;
        socket.emit('setupGame', {
            isClientHost: true,
            myAlliance: "WHITE"
        });
    } else {
        players[1].alliance = "BLACK";

        socket.emit('getPlayers', { players: players });
        console.log("Connecting to game host...");
        socket.emit('setupGame', {
            isClientHost: false,
            myAlliance: "BLACK"
        });
    }

    socket.on('playerReady', (data) => {
        console.log("players length: " + players.length);

        console.log("data");
        for (let i = 0; i < data.length; i++) {
            parsePieceDataString(data[i])
            console.log(data[i]);
        }

        console.log("boardConfig");
        boardConfig.forEach((function(value, key) {
            console.log(key + ' = ' +
                        "rank=" + value.rank +
                        " ;tileId=" + value.tileId +
                        " ;owner=" + value.owner +
                        " ;alliance=" + value.alliance);
        }))

        // Start game
        if (players.length >= 2) {
            startGame();

            io.sockets.emit('playersHandshake', {
                players: players,
                firstMoveMaker: firstMoveMakerAlliance,
                boardConfig: Array.from(boardConfig)
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
            console.log("[ " + moveHistory.length + " ] playerId: " + newMove.playerId +
                        "; turnId: "+ newMove.turnId + "; from: " + newMove.srcTileId +
                        "; to: " + newMove.tgtTileId);
            socket.broadcast.emit('makeTurnMove', data);
        }
    });

});

function startGame() {
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
    boardConfig = new Map();
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

function piece(rank, tileId, owner, alliance) {
    this.rank = rank;
    this.tileId = tileId;
    this.owner = owner;
    this.alliance = alliance;
}

function parsePieceDataString(data) {
    let rankKey = 'rank=';
    let tileIdKey = 'tileId=';
    let ownerKey = 'owner=';
    let allianceKey = 'alliance=';

    let rankIdx = data.indexOf(rankKey);
    let tileIdIdx = data.indexOf(tileIdKey);
    let ownerIdx = data.indexOf(ownerKey);
    let allianceIdx = data.indexOf(allianceKey);

    let rank = data.substring(rankIdx + rankKey.length, tileIdIdx - 1);
    let tileId = parseInt(data.substring(tileIdIdx + tileIdKey.length, ownerIdx - 1));
    let owner = data.substring(ownerIdx + ownerKey.length, allianceIdx - 1);
    let alliance = data.substring(allianceIdx + allianceKey.length, data.length);

    boardConfig.set(tileId, new piece(rank, tileId, owner, alliance));
}

