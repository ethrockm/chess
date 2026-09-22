package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor color;
    ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        this.type = type;
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
        Collection<ChessMove> moves = new ArrayList<>();
        if(type == PieceType.PAWN) {
            if(getTeamColor() == ChessGame.TeamColor.WHITE) {
                ChessPosition target = myPosition.addOffset(1, 0); // Move
                if (isValidMove(myPosition, target, board, getTeamColor()) && board.getPiece(target) == null) {
                    if(target.getRow() == 8){
                        moves.add(new ChessMove(myPosition, target, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, target, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, target, PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, target, PieceType.ROOK));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, target, null));
                    }
                    target = myPosition.addOffset(2, 0);
                    if(myPosition.getRow() == 2) {
                        if (isValidMove(myPosition, target, board, getTeamColor()) && board.getPiece(target) == null) {
                            moves.add(new ChessMove(myPosition, target, null));
                        }
                    }
                }
                target = myPosition.addOffset(1, 1); // Capture
                if (isValidTarget(myPosition, target, board, getTeamColor())) {
                    if(target.getRow() == 8){
                        moves.add(new ChessMove(myPosition, target, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, target, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, target, PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, target, PieceType.ROOK));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, target, null));
                    }
                }
                target = myPosition.addOffset(1, -1); // Capture
                if (isValidTarget(myPosition, target, board, getTeamColor())) {
                    if(target.getRow() == 8){
                        moves.add(new ChessMove(myPosition, target, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, target, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, target, PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, target, PieceType.ROOK));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, target, null));
                    }
                }
            }
            if(getTeamColor() == ChessGame.TeamColor.BLACK) {
                ChessPosition target = myPosition.addOffset(-1, 0);
                if (isValidMove(myPosition, target, board, getTeamColor()) && board.getPiece(target) == null) {
                    if(target.getRow() == 1){
                        moves.add(new ChessMove(myPosition, target, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, target, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, target, PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, target, PieceType.ROOK));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, target, null));
                    }
                    if(myPosition.getRow() == 7) {
                        target = myPosition.addOffset(-2, 0);
                        if (isValidMove(myPosition, target, board, getTeamColor()) && board.getPiece(target) == null) {
                            moves.add(new ChessMove(myPosition, target, null));
                        }
                    }
                }
                target = myPosition.addOffset(-1, 1); // Capture
                if (isValidTarget(myPosition, target, board, getTeamColor())) {
                    if(target.getRow() == 1){
                        moves.add(new ChessMove(myPosition, target, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, target, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, target, PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, target, PieceType.ROOK));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, target, null));
                    }
                }
                target = myPosition.addOffset(-1, -1); // Capture
                if (isValidTarget(myPosition, target, board, getTeamColor())) {
                    if(target.getRow() == 1){
                        moves.add(new ChessMove(myPosition, target, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, target, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, target, PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, target, PieceType.ROOK));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, target, null));
                    }
                }
            }
        }
        if(type == PieceType.KING) {
            int[][] offsets = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            for (int[] offset : offsets) {
                ChessPosition target = myPosition.addOffset(offset[0], offset[1]);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                }
            }
        }
        if(type == PieceType.KNIGHT) {
            int[][] offsets = {{2, 1}, {2, -1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {1, -2}, {-1, -2}};
            for (int[] offset : offsets) {
                ChessPosition target = myPosition.addOffset(offset[0], offset[1]);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                }
            }
        }
        if(type == PieceType.ROOK) {
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(0,i);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(0,-i);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(i,0);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(-i,0);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
        }
        if(type == PieceType.BISHOP) {
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(i,i);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(i,-i);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(-i,i);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(-i,-i);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
        }
        if(type == PieceType.QUEEN) {
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(0,i);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(0,-i);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(i,0);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(-i,0);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(i,i);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(i,-i);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(-i,i);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            for (int i = 1; i < 8; i++) {
                ChessPosition target = myPosition.addOffset(-i,-i);
                if (isValidMove(myPosition, target, board, getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else{
                    break;
                }
            }
        }
        return moves;
    }

    private boolean isValidTarget(ChessPosition myPosition, ChessPosition target, ChessBoard board, ChessGame.TeamColor teamColor) {
        if(target.getColumn() > 8 || target.getColumn() < 1 || target.getRow() > 8 || target.getRow() < 1){
            return false;
        }
        if(board.getPiece(target) != null && board.getPiece(target).getTeamColor() != getTeamColor()){
            return true;
        }
        return false;
    }

    private boolean isValidMove(ChessPosition myPosition, ChessPosition target, ChessBoard board, ChessGame.TeamColor teamColor) {
        if(target.getColumn() > 8 || target.getColumn() < 1 || target.getRow() > 8 || target.getRow() < 1){
            return false;
        }
        if(board.getPiece(target) == null || board.getPiece(target).getTeamColor() != getTeamColor()){
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }
}
