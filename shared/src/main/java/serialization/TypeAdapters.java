package serialization;

import chess.ChessGame;
import chess.ChessPiece;
import chess.pieces.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

public class TypeAdapters {

    public static void main(String[] args){
        String json = new Gson().toJson(new Knight(ChessGame.TeamColor.BLACK));
        Gson testMe = new GsonBuilder().registerTypeAdapter(ChessPiece.class, TypeAdapters.pieceDeserializer()).create();
        ChessPiece piece = testMe.fromJson(json, ChessPiece.class);
        System.out.println(piece);
    }

    public static JsonDeserializer<ChessPiece> pieceDeserializer(){
        return new JsonDeserializer<ChessPiece>() {
            @Override
            public ChessPiece deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
                    throws JsonParseException {
                JsonObject obj = jsonElement.getAsJsonObject();
                JsonPrimitive prim = obj.getAsJsonPrimitive("type");
                if (prim == null){
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
            }
        };
    }

}
