package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Bishop extends ChessPiece {

    public Bishop(ChessGame.TeamColor color){
        super(color, PieceType.BISHOP);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        moves.addAll(moveInDirection(board, myPosition, VerticalDirection.UP, HorizontalDirection.RIGHT));
        moves.addAll(moveInDirection(board, myPosition, VerticalDirection.UP, HorizontalDirection.LEFT));
        moves.addAll(moveInDirection(board, myPosition, VerticalDirection.DOWN, HorizontalDirection.RIGHT));
        moves.addAll(moveInDirection(board, myPosition, VerticalDirection.DOWN, HorizontalDirection.LEFT));
        return moves;
    }

    @Override
    public String toString() {
        return getTeamColor() == ChessGame.TeamColor.WHITE ? "wb" : "bb";
    }
}
