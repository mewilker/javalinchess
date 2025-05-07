package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Rook extends ChessPiece {
    public Rook(ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.ROOK);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        moves.addAll(moveInDirection(board, myPosition, VerticalDirection.UP, null));
        moves.addAll(moveInDirection(board, myPosition, null, HorizontalDirection.LEFT));
        moves.addAll(moveInDirection(board, myPosition, null, HorizontalDirection.RIGHT));
        moves.addAll(moveInDirection(board, myPosition, VerticalDirection.DOWN, null));
        return moves;
    }

    @Override
    public String toString() {
        return getTeamColor() == ChessGame.TeamColor.WHITE ? "wr" : "br";
    }
}
