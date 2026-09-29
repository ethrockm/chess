package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
TeamColor turn;
ChessBoard board;
    public ChessGame() {
        turn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece moves = new ChessPiece(getBoard().getPiece(startPosition).color,getBoard().getPiece(startPosition).type);
        if(board.getPiece(startPosition) == null){
            return null;
        }
        return moves.pieceMoves(getBoard(),startPosition);
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //if(!isInCheck() && !isInCheckmate())
        if(board.getPiece(move.getStartPosition()).getTeamColor() != turn){
            throw new InvalidMoveException("Invalid Move");
        }
        board.addPiece(move.getEndPosition(),board.getPiece(move.getStartPosition()));
        board.addPiece(move.getStartPosition(),null);
        if(getTeamTurn() == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }
        else {
            setTeamTurn(TeamColor.WHITE);
        }
        //throw new InvalidMoveException("Invalid Move");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king;
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                if(board.getPiece(new ChessPosition(i,j)) != null && board.getPiece(new ChessPosition(i,j)).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(new ChessPosition(i,j)).getTeamColor() == teamColor){
                    king = new ChessPosition(i,j);
                    for(int i1 = 1; i1 <= 8; i1++){
                        for(int j1 = 1; j1 <= 8; j1++){
                            if(board.getPiece(new ChessPosition(i1,j1)) != null) {
                                for (ChessMove moves : board.getPiece(new ChessPosition(i1, j1)).pieceMoves(board, new ChessPosition(i1, j1))) {
                                    if (moves.getEndPosition().equals(king)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                    break;
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
        if(!isInCheck(teamColor)){
            return false;
        }
        ChessPosition king = null;
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                if(board.getPiece(new ChessPosition(i,j)) != null && board.getPiece(new ChessPosition(i,j)).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(new ChessPosition(i,j)).getTeamColor() == teamColor){
                    king = new ChessPosition(i,j);
                    
                }
            }
        }
        if(king == null){
            return false;
        }
        int moveCount = board.getPiece(king).pieceMoves(board, king).size();
        for(ChessMove kingMoves : board.getPiece(king).pieceMoves(board, king)){
            outerLoop:
            for (int i1 = 1; i1 <= 8; i1++) {
                for (int j1 = 1; j1 <= 8; j1++) {
                    if (board.getPiece(new ChessPosition(i1, j1)) != null) {
                        for (ChessMove moves : board.getPiece(new ChessPosition(i1, j1)).pieceMoves(board, new ChessPosition(i1, j1))) {
                            if (moves.getEndPosition().equals(kingMoves.getEndPosition())) {
                                moveCount = moveCount - 1;
                                break outerLoop;
                            }
                        }
                    }
                }
            }
        }
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                if(board.getPiece(new ChessPosition(i,j)) != null && board.getPiece(new ChessPosition(i,j)).getTeamColor() == teamColor){
                    for (ChessMove moves : board.getPiece(new ChessPosition(i, j)).pieceMoves(board, new ChessPosition(i, j))) {
                        ChessPiece targetPiece = board.getPiece(new ChessPosition(moves.getEndPosition().getRow(),moves.getEndPosition().getColumn()));
                        board.addPiece(new ChessPosition(moves.getEndPosition().getRow(),moves.getEndPosition().getColumn()),board.getPiece(new ChessPosition(i,j)));
                        board.addPiece(new ChessPosition(moves.getStartPosition().getRow(),moves.getStartPosition().getColumn()),null);
                        boolean stillInCheck = isInCheck(teamColor);
                        board.addPiece(new ChessPosition(moves.getStartPosition().getRow(),moves.getStartPosition().getColumn()),board.getPiece(new ChessPosition(moves.getEndPosition().getRow(),moves.getEndPosition().getColumn())));
                        board.addPiece(new ChessPosition(moves.getEndPosition().getRow(),moves.getEndPosition().getColumn()),targetPiece);
                        if(!stillInCheck){
                            return false;
                        }
                    }
                    break;
                }
            }
        }
        if(moveCount == 0){
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        ChessPosition king = null;
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                if(board.getPiece(new ChessPosition(i,j)) != null && board.getPiece(new ChessPosition(i,j)).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(new ChessPosition(i,j)).getTeamColor() == teamColor){
                    king = new ChessPosition(i,j);
                }
            }
        }
        if(king == null){
            return false;
        }
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                if(board.getPiece(new ChessPosition(i,j)) != null && board.getPiece(new ChessPosition(i,j)).getTeamColor() == teamColor){
                    if(board.getPiece(new ChessPosition(i,j)).pieceMoves(board, new ChessPosition(i,j)) != null && !new ChessPosition(i, j).equals(king)){
                        return false;
                    }
                }
            }
        }
        int moveCountKing = board.getPiece(king).pieceMoves(board, king).size();
        for(ChessMove kingMoves : board.getPiece(king).pieceMoves(board, king)){
            outerLoop:
            for (int i1 = 1; i1 <= 8; i1++) {
                for (int j1 = 1; j1 <= 8; j1++) {
                    if (board.getPiece(new ChessPosition(i1, j1)) != null) {
                        for (ChessMove moves : board.getPiece(new ChessPosition(i1, j1)).pieceMoves(board, new ChessPosition(i1, j1))) {
                            if (moves.getEndPosition().equals(kingMoves.getEndPosition())) {
                                moveCountKing = moveCountKing - 1;
                                break outerLoop;
                            }
                        }
                    }
                }
            }
        }
        if(moveCountKing == 0){
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, board);
    }
}
