package chess.pieces;

import chess.ChessGame;
import chess.ChessPiece;

public class Pawn extends ChessPiece {
    public Pawn(ChessGame.TeamColor pieceColor) {
        super(pieceColor,PieceType.PAWN);
    }

    @Override
    public String toString (){
        return color == ChessGame.TeamColor.WHITE ? "wp" : "bp";
    }
}
