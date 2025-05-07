package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Pawn extends ChessPiece {
    public Pawn(ChessGame.TeamColor pieceColor) {
        super(pieceColor,PieceType.PAWN);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessMove move = forwardTwo(board, myPosition);
        if (move != null){
            moves.add(move);
        }
        moves.addAll(forwardOne(board, myPosition));
        moves.addAll(captures(board, myPosition));
        return moves;
    }

    private ChessMove forwardTwo(ChessBoard board, ChessPosition myPosition){
        if (getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (myPosition.getRow() == 2) {
                ChessPosition inFront = new ChessPosition(3, myPosition.getColumn());
                ChessPosition inspect = new ChessPosition(4, myPosition.getColumn());
                if (board.getPiece(inspect) == null && board.getPiece(inFront) == null) {
                    return new ChessMove(myPosition, inspect, null);
                }
            }
        }
        if (getTeamColor() == ChessGame.TeamColor.BLACK){
            if (myPosition.getRow() == 7) {
                ChessPosition inFront = new ChessPosition(6, myPosition.getColumn());
                ChessPosition inspect = new ChessPosition(5, myPosition.getColumn());
                if (board.getPiece(inspect) == null && board.getPiece(inFront) == null) {
                    return new ChessMove(myPosition, inspect, null);
                }
            }
        }
        return null;
    }

    private HashSet<ChessMove> forwardOne(ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> moves = new HashSet<>();
        ChessPosition inspect;
        if (getTeamColor() == ChessGame.TeamColor.WHITE) {
            inspect = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        }
        else{
            inspect = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        }
        if (board.getPiece(inspect) == null) {
            moves.addAll(checkPromotes(myPosition, inspect));
        }
        return moves;
    }

    private HashSet<ChessMove> captures(ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> moves = new HashSet<>();
        ChessPosition inspect;
        if (getTeamColor() == ChessGame.TeamColor.WHITE) {
            inspect = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        }
        else{
            inspect = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
        }
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                inspect = new ChessPosition(inspect.getRow(), myPosition.getColumn()-1);
            }
            else {
                inspect = new ChessPosition(inspect.getRow(), myPosition.getColumn()+1);
            }
            if (board.isOnBoard(inspect)) {
                ChessPiece capture = board.getPiece(inspect);
                if (capture != null && capture.getTeamColor() != getTeamColor()) {
                    moves.addAll(checkPromotes(myPosition, inspect));
                }
            }
        }
        return moves;
    }

    private HashSet<ChessMove> checkPromotes(ChessPosition myPosition, ChessPosition toMove){
        HashSet<ChessMove> promotions = new HashSet<>();
        int comp;
        if (getTeamColor() == ChessGame.TeamColor.WHITE) {
            comp = 8;
        }
        else{
            comp = 1;
        }
        if (toMove.getRow() == comp) {
            promotions.add(new ChessMove(myPosition, toMove, PieceType.QUEEN));
            promotions.add(new ChessMove(myPosition, toMove, PieceType.BISHOP));
            promotions.add(new ChessMove(myPosition, toMove, PieceType.KNIGHT));
            promotions.add(new ChessMove(myPosition, toMove, PieceType.ROOK));
        }
        else{
            promotions.add(new ChessMove(myPosition, toMove, null));
        }
        return promotions;
    }

    @Override
    public String toString (){
        return getTeamColor() == ChessGame.TeamColor.WHITE ? "wp" : "bp";
    }
}
