package com.markl.game.network;

import com.badlogic.gdx.Gdx;
import com.markl.game.engine.board.Alliance;
import com.markl.game.ui.screen.GameScreen;

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
    }).on("socketID", new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
        try {
          String id = data.getString("id");
          Gdx.app.log("SocketIO", "My ID: " + id);
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

          Gdx.app.log("SocketIO", "isClientHost: " + isClientHost);
          Gdx.app.log("SocketIO", "Setting up game");
          Gdx.app.log("SocketIO", "isHost" + isClientHost);

          if (isClientHost) {
            gameScreen.gameState.setRandomMyAlliance();
            if (gameScreen.gameState.getMyAlliance() == Alliance.WHITE)
              gameScreen.initBoard(false);
            else
              gameScreen.initBoard(true);

            gameScreen.initGame();

            outData.put("myAlliance", gameScreen.gameState.getMyAlliance().getValue());
            outData.put("firstMoveMaker", gameScreen.gameState.getFirstMoveMaker().getValue());
            socket.emit("startGame", outData);
          } else {
            if (inData.get("takenAlliance").equals("WHITE")) {
              gameScreen.gameState.setMyAlliance(Alliance.BLACK);
              gameScreen.initBoard(true);
            } else {
              gameScreen.gameState.setMyAlliance(Alliance.WHITE);
              gameScreen.initBoard(false);
            }

            if (inData.get("firstMoveMaker").equals("WHITE"))
              gameScreen.initGame(Alliance.WHITE);
            else
              gameScreen.initGame(Alliance.BLACK);
          }
        } catch (JSONException e) {
          Gdx.app.log("SocketIO", "Error setting up game");
        }
      }
    }).on("newPlayer", new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
        try {
          String id = data.getString("id");
          Gdx.app.log("SocketIO", "New Player Connect: " + id);
        } catch (JSONException e) {
          Gdx.app.log("SocketIO", "Error getting New Player ID");
        }
      }
    }).on("makeTurnMove", new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
        if (data != null) {
          try {
            Gdx.app.log("SocketIO", "Move received!");
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
      data.put("turnId", turnId);
      data.put("srcTileId", srcTileId);
      data.put("tgtTileId", tgtTileId);
      socket.emit("makeTurnMove", data);
      Gdx.app.log("SocketIO", "Move sent!");
    } catch(JSONException e) {
      Gdx.app.log("SOCKET.IO", "Error sending makeTurnMove update");
    }
  }
}
