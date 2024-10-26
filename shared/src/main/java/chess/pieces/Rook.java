package chess.pieces;

import chess.ChessGame;
import chess.ChessPiece;

public class Rook extends ChessPiece {
    public Rook(ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.ROOK);
    }

    @Override
    public String toString (){
        return color == ChessGame.TeamColor.WHITE ? "wr" : "br";
    }
}
