package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard board = new ChessBoard();
    TeamColor currTurn = TeamColor.WHITE;

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currTurn = team;
    }

    public TeamColor getEnemyColor(TeamColor color) {
        if (color == TeamColor.WHITE) {
            return TeamColor.BLACK;
        }
        return TeamColor.WHITE;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (startPosition == null){
            return null;
        }
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPiece piece = board.getPiece(startPosition);
        if (piece != null) {
            for (ChessMove move : piece.pieceMoves(board, startPosition)) {
                //clone board
                ChessBoard saveBoard = new ChessBoard(board);
                //move piece on board
                try {
                    board.makeMove(move);
                    //if not in check after, add to valid moves
                    if (!isInCheck(piece.getTeamColor())) {
                        validMoves.add(move);
                    }
                } catch (InvalidMoveException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace(System.err);
                }
                setBoard(saveBoard);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("There is no piece at " + move.getStartPosition().toString());
        }
        if (piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Cannot move " + piece.getTeamColor().toString().toLowerCase() + " piece on " +
                    getTeamTurn().toString().toLowerCase() + "'s turn.");
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves.contains(move)) {
            board.makeMove(move);
            advanceTeamTurn();
            return;
        }
        throw new InvalidMoveException("Cannot make move");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        HashSet<ChessPosition> oppositionPieces = board.getTeamPiecePositions(getEnemyColor(teamColor));
        for (ChessPosition piecePos : oppositionPieces) {
            Collection<ChessMove> pieceMoves = board.getPiece(piecePos).pieceMoves(board, piecePos);
            for (ChessMove move : pieceMoves) {
                if (move.getEndPosition().equals(board.getKingPosition(teamColor))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor) || hasMovesAvailable(teamColor)) {
            return false;
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (hasMovesAvailable(teamColor) || isInCheck(teamColor)) {
            return false;
        }
        return true;
    }

    public boolean hasMovesAvailable(TeamColor teamColor) {
        HashSet<ChessPosition> piecePositions = new HashSet<>(board.getTeamPiecePositions(teamColor));
        for (ChessPosition position : piecePositions) {
            Collection<ChessMove> moves = validMoves(position);
            if (!moves.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void advanceTeamTurn() {
        if (getTeamTurn() == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && currTurn == chessGame.currTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currTurn);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }
}
