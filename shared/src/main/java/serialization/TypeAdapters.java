package serialization;

import chess.ChessPiece;
import chess.pieces.*;
import com.google.gson.*;
import response.CreateGameResult;
import response.ListGamesResult;
import response.LoginResult;
import response.Result;
import websocket.commands.*;

import java.util.ArrayList;

public class TypeAdapters {

    public static void main(String[] args) {
        Result first = new ListGamesResult(new ArrayList<>());
        //first.setMessage("I have a result");
        String json = new Gson().toJson(first);
        Gson testMe = new GsonBuilder().registerTypeAdapter(Result.class, TypeAdapters.resultDeserializer()).create();
        Result result = testMe.fromJson(json, Result.class);
        ListGamesResult finish = (ListGamesResult) result;
        System.out.println(finish.getGames());
    }

    public static JsonDeserializer<ChessPiece> pieceDeserializer() {
        return (jsonElement, type, context) -> {
            JsonObject obj = jsonElement.getAsJsonObject();
            JsonPrimitive prim = obj.getAsJsonPrimitive("type");
            if (prim == null) {
                throw new JsonParseException("Could not deserialize ChessPiece due to no piece type");
            }
            String pieceType = prim.getAsString();
            return switch (ChessPiece.PieceType.valueOf(pieceType)) {
                case PAWN -> context.deserialize(jsonElement, Pawn.class);
                case ROOK -> context.deserialize(jsonElement, Rook.class);
                case BISHOP -> context.deserialize(jsonElement, Bishop.class);
                case QUEEN -> context.deserialize(jsonElement, Queen.class);
                case KNIGHT -> context.deserialize(jsonElement, Knight.class);
                case KING -> context.deserialize(jsonElement, King.class);
            };
        };
    }

    //TODO: this is not the prettiest, most maintainable way to determine result type. Fix it.
    public static JsonDeserializer<Result> resultDeserializer() {
        return (jsonElement, type, context) -> {
            JsonObject obj = jsonElement.getAsJsonObject();
            JsonPrimitive prim = obj.getAsJsonPrimitive("message");
            if (prim != null) {
                return new Gson().fromJson(jsonElement, Result.class);
            }
            prim = obj.getAsJsonPrimitive("authToken");
            if (prim != null) {
                return context.deserialize(jsonElement, LoginResult.class);
            }
            prim = obj.getAsJsonPrimitive("gameID");
            if (prim != null) {
                return context.deserialize(jsonElement, CreateGameResult.class);
            }
            JsonArray array = obj.getAsJsonArray("games");
            if (array != null) {
                return context.deserialize(jsonElement, ListGamesResult.class);
            }
            return null;
        };
    }

    public static JsonDeserializer<UserGameCommand> commandDeserializer(){
        return ((jsonElement, type, context) -> {
            JsonObject obj = jsonElement.getAsJsonObject();
            JsonPrimitive prim = obj.getAsJsonPrimitive("commandType");
            UserGameCommand.CommandType command = UserGameCommand.CommandType.valueOf(prim.getAsString());
            return switch (command){
                case MAKE_MOVE -> context.deserialize(jsonElement, MakeMoveCommand.class);
                case LEAVE -> context.deserialize(jsonElement, LeaveCommand.class);
                case CONNECT -> context.deserialize(jsonElement, ConnectCommand.class);
                case RESIGN -> context.deserialize(jsonElement, ResignCommand.class);
            };
        });
    }

}
