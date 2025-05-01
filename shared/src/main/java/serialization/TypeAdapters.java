package serialization;

import chess.ChessPiece;
import chess.pieces.*;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class TypeAdapters {
    public static TypeAdapter<ChessPiece> pieceTypeAdapter(){
        return new TypeAdapter<>() {
            private final Gson GSON = new Gson();
            @Override
            public void write(JsonWriter jsonWriter, ChessPiece chessPiece) throws IOException {
                switch (chessPiece.getPieceType()){
                    case KNIGHT -> GSON.getAdapter(Knight.class).write(jsonWriter, (Knight) chessPiece);
                    case QUEEN -> GSON.getAdapter(Queen.class).write(jsonWriter,(Queen) chessPiece);
                    case KING -> GSON.getAdapter(King.class).write(jsonWriter, (King) chessPiece);
                    case ROOK -> GSON.getAdapter(Rook.class).write(jsonWriter,(Rook) chessPiece);
                    case BISHOP -> GSON.getAdapter(Bishop.class).write(jsonWriter, (Bishop) chessPiece);
                    case PAWN -> GSON.getAdapter(Pawn.class).write(jsonWriter, (Pawn) chessPiece);
                }
            }

            @Override
            public ChessPiece read(JsonReader jsonReader) throws IOException {
                return null;
            }
        };
    }

}
