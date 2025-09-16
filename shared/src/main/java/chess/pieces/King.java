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
        HashSet<ChessPosition> positions = new HashSet<>();
        //upright
        positions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1));
        //downright
        positions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));
        //upleft
        positions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
        //downleft
        positions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));
        //up
        positions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()));
        //left
        positions.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1));
        //down
        positions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()));
        //right
        positions.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1));
        for (ChessPosition inspect : positions) {
            if (canTake(board, inspect)) {
                moves.add(new ChessMove(myPosition, inspect, null));
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        return getTeamColor() == ChessGame.TeamColor.WHITE ? "wk" : "bk";
    }
}
