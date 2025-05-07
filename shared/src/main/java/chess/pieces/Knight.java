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
        ChessPosition inspect = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1);
        if (canTake(board, inspect)){
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1);
        if (canTake(board, inspect)){
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1);
        if (canTake(board, inspect)){
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1);
        if (canTake(board, inspect)){
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2);
        if (canTake(board, inspect)){
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2);
        if (canTake(board, inspect)){
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2);
        if (canTake(board, inspect)){
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        inspect = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2);
        if (canTake(board, inspect)){
            moves.add(new ChessMove(myPosition, inspect, null));
        }
        //TODO: there has got to be a prettier way to populate this
        return moves;
    }

    //take in 2d int array

    @Override
    public String toString (){
        return getTeamColor() == ChessGame.TeamColor.WHITE ? "wn" : "bn";
    }
}
