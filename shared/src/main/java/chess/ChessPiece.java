package chess;

import chess.pieces.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor color;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        this.type = type;
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (type) {
            case BISHOP -> new Bishop(color).pieceMoves(board, myPosition);
            case ROOK -> new Rook(color).pieceMoves(board, myPosition);
            case QUEEN -> new Queen(color).pieceMoves(board, myPosition);
            case KNIGHT -> new Knight(color).pieceMoves(board, myPosition);
            case KING -> new King(color).pieceMoves(board, myPosition);
            case PAWN -> new Pawn(color).pieceMoves(board, myPosition);
        };
    }

    protected HashSet<ChessMove> moveInDirection(ChessBoard board, ChessPosition position, VerticalDirection vertical,
                                                 HorizontalDirection horizontal) {
        HashSet<ChessMove> moves = new HashSet<>();
        int upCount;
        int rightCount;
        ChessPosition inspect = new ChessPosition(position.getRow(), position.getColumn());
        while (board.isOnBoard(inspect)) {
            if (!inspect.equals(position)) {
                ChessPiece takePiece = board.getPiece(inspect);
                if (takePiece == null) {
                    moves.add(new ChessMove(position, inspect, null));
                } else if (takePiece.getTeamColor() == this.color) {
                    return moves;
                } else {
                    moves.add(new ChessMove(position, inspect, null));
                    return moves;
                }
            }
            switch (vertical) {
                case UP -> upCount = 1;
                case DOWN -> upCount = -1;
                case null, default -> upCount = 0;
            }
            switch (horizontal) {
                case RIGHT -> rightCount = 1;
                case LEFT -> rightCount = -1;
                case null, default -> rightCount = 0;
            }
            inspect = new ChessPosition(inspect.getRow() + upCount, inspect.getColumn() + rightCount);
        }

        return moves;
    }

    protected boolean canTake(ChessBoard board, ChessPosition toTake) {
        if (!board.isOnBoard(toTake)) {
            return false;
        }
        ChessPiece piece = board.getPiece(toTake);
        if (piece == null || piece.getTeamColor() != color) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece piece)) return false;
        return color == piece.color && type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    @Override
    public String toString() {
        return switch (type) {
            case KING -> new King(color).toString();
            case PAWN -> new Pawn(color).toString();
            case ROOK -> new Rook(color).toString();
            case QUEEN -> new Queen(color).toString();
            case BISHOP -> new Bishop(color).toString();
            case KNIGHT -> new Knight(color).toString();
        };
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    protected enum VerticalDirection {
        UP,
        DOWN
    }

    protected enum HorizontalDirection {
        LEFT,
        RIGHT
    }
}
