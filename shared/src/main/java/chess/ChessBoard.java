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
    private HashSet<ChessPosition> whitePieces = new HashSet<>();
    private HashSet<ChessPosition> blackPieces = new HashSet<>();
    private ChessPosition whiteKingPos = null;
    private ChessPosition blackKingPos = null;

    public ChessBoard() {}

    public ChessBoard(ChessBoard other){
        this.blackKingPos = other.blackKingPos;
        this.whiteKingPos = other.whiteKingPos;
        this.blackPieces = new HashSet<>(other.blackPieces);
        this.whitePieces = new HashSet<>(other.whitePieces);
        for (int i = 0; i < 8; i++){
            System.arraycopy(other.squares[i], 0, this.squares[i], 0, 8);
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                whitePieces.add(position);
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    whiteKingPos = position;
                }
            } else {
                blackPieces.add(position);
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    blackKingPos = position;
                }
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

    public HashSet<ChessPosition> getTeamPiecePositions(ChessGame.TeamColor color){
        if (color == ChessGame.TeamColor.WHITE){
            return whitePieces;
        }
        return blackPieces;
    }

    /**
     * Edits squares based on ChessMove given to it.
     * <p>
     * Does not validate that the move is a valid piece move.
     * @param move
     * @throws InvalidMoveException when starting position is empty or promotion is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException{
        ChessPiece piece = getPiece(move.getStartPosition());
        if (piece == null){
            throw new InvalidMoveException("There is no piece at " + move.getStartPosition().toString());
        }
        ChessPiece captured = getPiece(move.getEndPosition());
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            whitePieces.remove(move.getStartPosition());
            whitePieces.add(move.getEndPosition());
            if (piece.getPieceType() == ChessPiece.PieceType.KING){
                whiteKingPos = move.getEndPosition();
            }
            if (captured != null){
                blackPieces.remove(move.getEndPosition());
            }
        }
        else{
            blackPieces.remove(move.getStartPosition());
            blackPieces.add(move.getEndPosition());
            if (piece.getPieceType() == ChessPiece.PieceType.KING){
                blackKingPos = move.getEndPosition();
            }
            if (captured != null){
                whitePieces.remove(move.getEndPosition());
            }
        }
        addPiece(move.getStartPosition(), null);
        switch (move.getPromotionPiece()){
            case BISHOP -> addPiece(move.getEndPosition(), new Bishop(piece.getTeamColor()));
            case ROOK -> addPiece(move.getEndPosition(), new Rook(piece.getTeamColor()));
            case QUEEN -> addPiece(move.getEndPosition(), new Queen(piece.getTeamColor()));
            case KNIGHT -> addPiece(move.getEndPosition(), new Knight(piece.getTeamColor()));
            case null -> addPiece(move.getEndPosition(), piece);
            default -> throw new InvalidMoveException("Cannot promote to type " + move.getPromotionPiece().toString());
        }
        //FIXME: if a bad promotion is given and the board isn't copied... this function will DOOM the program
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
