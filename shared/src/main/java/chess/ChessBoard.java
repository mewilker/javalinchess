package chess;

import chess.pieces.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] squares = new ChessPiece[8][8];
    private final HashSet<ChessPosition> whitePieces = new HashSet<>();
    private final HashSet<ChessPosition> blackPieces = new HashSet<>();
    private ChessPosition whiteKingPos = null;
    private ChessPosition blackKingPos = null;

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            whitePieces.add(position);
            if (piece.getPieceType() == ChessPiece.PieceType.KING){
                whiteKingPos = position;
            }
        }
        else{
            blackPieces.add(position);
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                blackKingPos = position;
            }
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    public boolean isOnBoard(ChessPosition pos){
        if (pos.getRow() > 8 || pos.getRow() < 1){
            return false;
        }
        if (pos.getColumn() > 8 || pos.getColumn() < 1){
            return false;
        }
        return true;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        addPiece(new ChessPosition(1,1), new Rook(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPosition(1,2), new Knight(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPosition(1,3), new Bishop(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPosition(1,4), new Queen(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPosition(1,5), new King(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPosition(1,6), new Bishop(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPosition(1,7), new Knight(ChessGame.TeamColor.WHITE));
        addPiece(new ChessPosition(1,8), new Rook(ChessGame.TeamColor.WHITE));

        for(int i = 1; i < 9; i++){
            addPiece(new ChessPosition(2, i), new Pawn(ChessGame.TeamColor.WHITE));
        }

        addPiece(new ChessPosition(8,1), new Rook(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPosition(8,2), new Knight(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPosition(8,3), new Bishop(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPosition(8,4), new Queen(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPosition(8,5), new King(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPosition(8,6), new Bishop(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPosition(8,7), new Knight(ChessGame.TeamColor.BLACK));
        addPiece(new ChessPosition(8,8), new Rook(ChessGame.TeamColor.BLACK));

        for(int i = 1; i < 9; i++){
            addPiece(new ChessPosition(7, i), new Pawn(ChessGame.TeamColor.BLACK));
        }
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor color){
        if (color == ChessGame.TeamColor.WHITE){
            return whiteKingPos;
        }
        return blackKingPos;
    }

    public HashSet<ChessPosition> getTeamPieces(ChessGame.TeamColor color){
        if (color == ChessGame.TeamColor.WHITE){
            return whitePieces;
        }
        return blackPieces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString(){
        StringBuilder build = new StringBuilder();
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 8; j++){
                build.append(printPiece(new ChessPosition(i,j)));
                build.append("|");
            }
            build.append(printPiece(new ChessPosition(i,8)));
            build.append("\n");
        }
        return build.toString();
    }

    private String printPiece(ChessPosition pos){
        StringBuilder build = new StringBuilder();
        ChessPiece piece = getPiece(pos);
        if (piece == null){
            build.append(" ");
        }
        else{
            build.append(getPiece(pos).toString());
        }
        return build.toString();
    }
}
