package chess.pieces;

import chess.ChessGame;
import chess.ChessPiece;

public class King extends ChessPiece {
    public King(ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.KING);
    }

    @Override
    public String toString (){
        return color == ChessGame.TeamColor.WHITE ? "wk" : "bk";
    }
}
