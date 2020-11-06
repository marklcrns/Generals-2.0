package com.markl.game.network;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.markl.game.engine.board.Alliance;
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
          // Add new player as enemy player
          if (otherPlayersSocketId.size() == 1) {
            if (gameScreen.gameState.getMyAlliance() == Alliance.WHITE) {
              gameScreen.gameState.setEnemyPlayer(Alliance.BLACK, newPlayerSocketId);
            } else {
              gameScreen.gameState.setEnemyPlayer(Alliance.WHITE, newPlayerSocketId);
            }
          }
          Gdx.app.log("SocketIO", "New Player Connect: " + newPlayerSocketId);
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
              otherPlayersSocketId.add(playerSocketId);
              Gdx.app.log("SocketIO", "New Player Connect: " + playerSocketId);
              // Add enemy player
              if (otherPlayersSocketId.size() == 1) {
                if (gameScreen.gameState.getMyAlliance() == Alliance.WHITE) {
                  gameScreen.gameState.setEnemyPlayer(Alliance.BLACK, playerSocketId);
                } else {
                  gameScreen.gameState.setEnemyPlayer(Alliance.WHITE, playerSocketId);
                }
              }
            }
            Gdx.app.log("SocketIO", "Existing players: " + players.length());
          }
        } catch (JSONException e) {
          Gdx.app.log("SocketIO", "Error getting ID");
        }
      }
    }).on("setupGame", new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        JSONObject inData = (JSONObject) args[0];
        JSONObject outData = new JSONObject();
        try {
          boolean isClientHost = inData.getBoolean("isClientHost");
          Gdx.app.log("SocketIO", "Setting up game");

          if (isClientHost) {
            gameScreen.gameState.setRandomMyAlliance();
            if (gameScreen.gameState.getMyAlliance() == Alliance.WHITE) {
              gameScreen.gameState.setMyPlayer(Alliance.WHITE, mySocketId);
              gameScreen.initBoard(false);
            } else {
              gameScreen.gameState.setMyPlayer(Alliance.BLACK, mySocketId);
              gameScreen.initBoard(true);
            }
          } else {
            if (inData.get("takenAlliance").equals("WHITE")) {
              gameScreen.gameState.setMyAlliance(Alliance.BLACK);
              gameScreen.gameState.setMyPlayer(Alliance.BLACK, mySocketId);
              gameScreen.initBoard(true);
            } else {
              gameScreen.gameState.setMyAlliance(Alliance.WHITE);
              gameScreen.gameState.setMyPlayer(Alliance.WHITE, mySocketId);
              gameScreen.initBoard(false);
            }
          }

          outData.put("myAlliance", gameScreen.gameState.getMyAlliance().getValue());
          socket.emit("playerReady", outData);
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
            String firstMoveMaker = data.getString("firstMoveMaker");
            // Check if both players are set
            if (gameScreen.gameState.getMyPlayer() != null &&
                gameScreen.gameState.getEnemyPlayer() != null) {
              if (firstMoveMaker.equals("WHITE"))
                gameScreen.initGame(Alliance.WHITE);
              else
                gameScreen.initGame(Alliance.BLACK);
              Gdx.app.log("SocketIO", "Game Started! First Move: " + firstMoveMaker);
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
            gameScreen.moveManager.makeMove(
                gameScreen.tilesUI.get(srcTileId),
                gameScreen.tilesUI.get(tgtTileId),
                false);
          } catch (JSONException e) {
            Gdx.app.log("SocketIO", "Error getting makeTurnMove data");
          }
        }
      }
    });
  }

  public void updateMove(int turnId, int srcTileId, int tgtTileId) {
    JSONObject data = new JSONObject();
    try {
      data.put("socketId", mySocketId);
      data.put("turnId", turnId);
      data.put("srcTileId", srcTileId);
      data.put("tgtTileId", tgtTileId);
      socket.emit("makeTurnMove", data);
    } catch(JSONException e) {
      Gdx.app.log("SOCKET.IO", "Error sending makeTurnMove update");
    }
  }
}
