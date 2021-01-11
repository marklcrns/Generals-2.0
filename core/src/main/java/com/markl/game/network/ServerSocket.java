package com.markl.game.network;

import static com.markl.game.engine.board.BoardUtils.pieceInstanceCreator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.markl.game.engine.board.Alliance;
import com.markl.game.engine.board.Move;
import com.markl.game.engine.board.Player;
import com.markl.game.engine.board.pieces.Piece;
import com.markl.game.ui.screen.GameScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * TODO Class Description.
 *
 * @author Mark Lucernas
 * Created on 10/27/2020.
 */
public class ServerSocket {

  private Socket socket;
  private String host;
  private int port;
  private GameScreen gameScreen;
  private String mySocketId;
  private List<String> otherPlayersSocketId = new ArrayList<>();

  public ServerSocket(String host, int port, GameScreen gameScreen) {
    this.host = host;
    this.port = port;
    this.gameScreen = gameScreen;
  }

  public void connectSocket() {
    try {
      socket = IO.socket("http://" + host + ":" + port);
      socket.connect();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void configSocketEvents() {
    socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        Gdx.app.log("SocketIO", "Connected");
      }
    }).on("socketId", new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
        try {
          mySocketId = data.getString("id");
          Gdx.app.log("SocketIO", "My ID: " + mySocketId);
        } catch (JSONException e) {
          Gdx.app.log("SocketIO", "Error getting ID");
        }
      }
    }).on("newPlayer", new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
        try {
          String newPlayerSocketId = data.getString("id");
          otherPlayersSocketId.add(newPlayerSocketId);

          Gdx.app.log("SocketIO", "New Player Connected: " + newPlayerSocketId);
        } catch (JSONException e) {
          Gdx.app.log("SocketIO", "Error getting New Player ID");
        }
      }
    }).on("getPlayers", new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
        try {
          JSONArray players = data.getJSONArray("players");
          // Add all existing players
          if (players.length() > 0) {
            String playerSocketId;
            for (int i = 0; i < players.length(); i++) {
              JSONObject player = players.getJSONObject(i);
              playerSocketId = player.getString("id");

              if (!playerSocketId.equals(mySocketId)) {
                otherPlayersSocketId.add(playerSocketId);
                Gdx.app.log("SocketIO", "Player Added: " + playerSocketId);
              }
            }
          }
        } catch (JSONException e) {
          Gdx.app.log("SocketIO", "Error getting ID");
        }
      }
    }).on("setupGame", new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        JSONObject inData = (JSONObject) args[0];
        JSONArray outDataArray = new JSONArray();
        try {
          Gdx.app.log("SocketIO", "Setting up game");

          Alliance myAlliance;
          if (inData.getString("myAlliance").equals("WHITE"))
            myAlliance = Alliance.WHITE;
          else
            myAlliance = Alliance.BLACK;

          gameScreen.initEngine();
          gameScreen.boardBuilder.createTerritoryRandomBuild(myAlliance);
          LinkedHashMap<Integer, Piece> boardConfig = gameScreen.boardBuilder.getBoardConfig();

          for (final Map.Entry<Integer, Piece> entry : boardConfig.entrySet()) {
            outDataArray.put(entry.getValue());
          }

          // TODO continue building territorial piece for player independently
          // then pass in board configs into outData

          socket.emit("playerReady", outDataArray);
        } catch (JSONException e) {
          Gdx.app.log("SocketIO", "Error setting up game");
        }
      }
    }).on("playersHandshake", new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
        if (data != null) {
          try {
            JSONArray players = data.getJSONArray("players");
            String firstMoveMaker = data.getString("firstMoveMaker");
            JSONArray boardConfig = data.getJSONArray("boardConfig");

            for (int i = 0; i < players.length(); i++) {
              JSONObject player = players.getJSONObject(i);
              // Set my player
              if (player.getString("id").equals(mySocketId)) {
                Gdx.app.log("SocketIO", "My player id: " + player.getString("id") + "; alliance: " + player.getString("alliance"));
                if (player.getString("alliance").equals("WHITE")) {
                  gameScreen.gog.setMyPlayer(Alliance.WHITE, player.getString("id"));
                  gameScreen.isBoardInverted = false;
                } else {
                  gameScreen.gog.setMyPlayer(Alliance.BLACK, player.getString("id"));
                  gameScreen.isBoardInverted = true;
                }
              // Set enemy player
              } else if (player.getString("id").equals(otherPlayersSocketId.get(0))) {
                Gdx.app.log("SocketIO", "Enemy player id: " + player.getString("id") + "; alliance: " + player.getString("alliance"));
                if (player.getString("alliance").equals("WHITE")) {
                  gameScreen.gog.setEnemyPlayer(Alliance.WHITE, player.getString("id"));
                } else {
                  gameScreen.gog.setEnemyPlayer(Alliance.BLACK, player.getString("id"));
                }
              }
            }

            // Check if both players are set
            if (gameScreen.gog.getMyPlayer() != null &&
                gameScreen.gog.getEnemyPlayer() != null) {

              Gdx.app.log("SocketIO", "Board Config: " + boardConfig.length());
              for (int i = 0; i < boardConfig.length(); i++) {
                JSONArray piece = (JSONArray) boardConfig.getJSONArray(i);

                String rank = piece.getJSONObject(1).getString("rank");
                int tileId = Integer.parseInt(piece.getJSONObject(1).getString("tileId"));
                Player owner;
                Alliance alliance;

                if (piece.getJSONObject(1).getString("owner").equals("WHITE"))
                  owner = gameScreen.gog.getPlayer(Alliance.WHITE);
                else
                  owner = gameScreen.gog.getPlayer(Alliance.BLACK);

                if (piece.getJSONObject(1).getString("alliance").equals("WHITE"))
                  alliance = Alliance.WHITE;
                else
                  alliance = Alliance.BLACK;

                Piece newPiece = pieceInstanceCreator(gameScreen.board, rank, owner, alliance);
                newPiece.setPieceTileId(tileId);

                gameScreen.boardBuilder.setPiece(newPiece, false);
                Gdx.app.log("SocketIO",
                            "rank=" + rank +
                            ";tileId=" + tileId +
                            ";owner=" + owner +
                            ";alliance=" + alliance);
              }

              gameScreen.initBoardUI();

              if (firstMoveMaker.equals("WHITE"))
                gameScreen.initGame(Alliance.WHITE);
              else
                gameScreen.initGame(Alliance.BLACK);

              Gdx.app.log("SocketIO", "Game Started! First Move: " + firstMoveMaker);
              Gdx.app.log("Game", "myAlliance: " + gameScreen.gog.getMyAlliance());
              Gdx.app.log("Game", "mySocketId: " + gameScreen.gog.getMyPlayer().getId());
              Gdx.app.log("Game", "enemySocketId: " + gameScreen.gog.getEnemyPlayer().getId());

            } else {
              Gdx.app.log("Game", "Game initialization failed. Missing player");
            }
          } catch (JSONException e) {
            Gdx.app.log("SocketIO", "ERROR players handshake");
          }
        }
      }
    }).on("makeTurnMove", new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
        if (data != null) {
          try {
            int turnId = data.getInt("turnId");
            int srcTileId = data.getInt("srcTileId");
            int tgtTileId = data.getInt("tgtTileId");
            gameScreen.moveManager.makeMove(srcTileId, tgtTileId, false);
          } catch (JSONException e) {
            Gdx.app.log("SocketIO", "Error getting makeTurnMove data");
          }
        }
      }
    });
  }

  public void updateMove(Move move) {
    JSONObject data = new JSONObject();
    try {
      int turnId = move.getTurnId();
      int srcTileId = move.getSrcTileId();
      int tgtTileId = move.getTgtTileId();
      int moveType = move.getMoveType().getValue();

      data.put("socketId", mySocketId);
      data.put("turnId", turnId);
      data.put("srcTileId", srcTileId);
      data.put("tgtTileId", tgtTileId);
      data.put("moveType", moveType);

      socket.emit("makeTurnMove", data);
    } catch(JSONException e) {
      Gdx.app.log("SOCKET.IO", "Error sending makeTurnMove update");
    }
  }
}
