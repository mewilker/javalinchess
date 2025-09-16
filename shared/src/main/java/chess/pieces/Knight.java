package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Knight extends ChessPiece {
    public Knight(ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.KNIGHT);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        HashSet<ChessPosition> positions = new HashSet<>();
        //add all vectors for knight
        positions.add(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1));
        positions.add(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1));
        positions.add(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1));
        positions.add(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1));
        positions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2));
        positions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2));
        positions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2));
        positions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2));
        for (ChessPosition inspect : positions){
            if (canTake(board, inspect)) {
                moves.add(new ChessMove(myPosition, inspect, null));
            }
        }
        return moves;
    }

    //take in 2d int array

    @Override
    public String toString() {
        return getTeamColor() == ChessGame.TeamColor.WHITE ? "wn" : "bn";
    }
}
