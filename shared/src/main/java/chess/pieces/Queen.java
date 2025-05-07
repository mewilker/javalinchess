package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Queen extends ChessPiece {
    public Queen(ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.QUEEN);
    }

    @Override
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> moves = new HashSet<>();
        moves.addAll(moveInDirection(board, myPosition, VerticalDirection.UP, null));
        moves.addAll(moveInDirection(board, myPosition, null, HorizontalDirection.LEFT));
        moves.addAll(moveInDirection(board, myPosition, null, HorizontalDirection.RIGHT));
        moves.addAll(moveInDirection(board, myPosition, VerticalDirection.DOWN, null));
        moves.addAll(moveInDirection(board, myPosition, VerticalDirection.UP, HorizontalDirection.RIGHT));
        moves.addAll(moveInDirection(board, myPosition, VerticalDirection.UP, HorizontalDirection.LEFT));
        moves.addAll(moveInDirection(board, myPosition, VerticalDirection.DOWN, HorizontalDirection.RIGHT));
        moves.addAll(moveInDirection(board, myPosition, VerticalDirection.DOWN, HorizontalDirection.LEFT));
        return moves;
    }

    @Override
    public String toString (){
        return getTeamColor() == ChessGame.TeamColor.WHITE ? "wq" : "bq";
    }
}
