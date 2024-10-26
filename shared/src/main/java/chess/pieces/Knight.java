package chess.pieces;

import chess.ChessGame;
import chess.ChessPiece;

public class Knight extends ChessPiece {
    public Knight(ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.KNIGHT);
    }

    @Override
    public String toString (){
        return color == ChessGame.TeamColor.WHITE ? "wn" : "bn";
    }
}
