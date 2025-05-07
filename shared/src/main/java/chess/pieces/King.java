package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class King extends ChessPiece {
    public King(ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.KING);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessPosition inspect = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        if (canTake(board, inspect)) {
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        if (canTake(board, inspect)) {
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        if (canTake(board, inspect)) {
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        if (canTake(board, inspect)) {
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        if (canTake(board, inspect)) {
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
        if (canTake(board, inspect)) {
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        if (canTake(board, inspect)) {
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
        if (canTake(board, inspect)) {
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        //TODO: there has got to be a prettier way to populate this
        return moves;
    }

    @Override
    public String toString() {
        return getTeamColor() == ChessGame.TeamColor.WHITE ? "wk" : "bk";
    }
}
