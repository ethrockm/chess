package chess;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    PieceType type;
    ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
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
        return pieceColor;
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
        if(myPosition.row > 8 || myPosition.row < 1 || myPosition.col > 8 || myPosition.col < 1){
            return moves;
        }
        if(type == PieceType.KING){
            int[][] offsets = { {1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {0, 1}, {0, -1}, {1, 0}, {-1, 0} };
            for (int[] offset : offsets) {
                ChessPosition target = myPosition.addOffset(offset[0], offset[1]);
                if (board.isValidMove(target, this.getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                }
            }
        }
        if(type == PieceType.KNIGHT){
            int[][] offsets = { {1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1} };
            for (int[] offset : offsets) {
                ChessPosition target = myPosition.addOffset(offset[0], offset[1]);
                if (board.isValidMove(target, this.getTeamColor())) {
                    moves.add(new ChessMove(myPosition, target, null));
                }
            }
        }
        if(type == PieceType.PAWN){
            if(getTeamColor() == ChessGame.TeamColor.WHITE){
                if(myPosition.getRow() == 2) {
                    ChessPosition target = myPosition.addOffset(0, 1);
                    if(board.getPiece(target) == null) {
                        target = myPosition.addOffset(0, 2);
                        if (board.isValidMove(target, this.getTeamColor())) {
                            moves.add(new ChessMove(myPosition, target, null));
                        }
                    }
                }
                ChessPosition target = myPosition.addOffset(0, 1);
                if (board.getPiece(target) == null) {
                    if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                        if(target.getRow() == 8){
                            moves.add(new ChessMove(myPosition, target, PieceType.QUEEN));
                        }
                        else {
                            moves.add(new ChessMove(myPosition, target, null));
                        }
                    }
                }
//                if (myPosition.getColumn() == 8){
//                    if(board.getPiece(myPosition.addOffset(1,-1)).getTeamColor() != this.getTeamColor() && board.getPiece(myPosition.addOffset(1,-1)) != null){
//                        if (board.isValidMove(myPosition.addOffset(1,-1), this.getTeamColor())) {
//                            moves.add(new ChessMove(myPosition, myPosition.addOffset(1,-1), null));
//                        }
//                    }
//                }
//                if (myPosition.getColumn() == 1){
//                    if(board.getPiece(myPosition.addOffset(1,1)) != null){
//                        if (board.isValidMove(myPosition.addOffset(1,1), this.getTeamColor())) {
//                            moves.add(new ChessMove(myPosition, myPosition.addOffset(1,1), null));
//                        }
//                    }
//                }
//                if(board.getPiece(myPosition.addOffset(1,1)) != null && myPosition.getColumn()+1 < 8){
//                        if (board.isValidMove(myPosition.addOffset(1,1), this.getTeamColor())) {
//                            moves.add(new ChessMove(myPosition, myPosition.addOffset(1,1), null));
//                        }
//                }
//                if(board.getPiece(myPosition.addOffset(1,-1)) != null && myPosition.getColumn()-1 > 0){
//                        if (board.isValidMove(myPosition.addOffset(1,-1), this.getTeamColor())) {
//                            moves.add(new ChessMove(myPosition, myPosition.addOffset(1,-1), null));
//                        }
//                }

            }
            if(getTeamColor() == ChessGame.TeamColor.BLACK) {
                if(myPosition.getRow() == 7) {
                    for (int i = 1; i < 3; i++) {
                        ChessPosition target = myPosition.addOffset(0, -i);
                        if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                            if(board.getPiece(target) != null){
                                break;
                            }
                            moves.add(new ChessMove(myPosition, target, null));
                        } else {
                            break;
                        }
                    }
                }
                ChessPosition target = myPosition.addOffset(0, -1);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                }

            }

        }
        if(type == PieceType.BISHOP) {
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(i, i);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(i, -i);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(-i, i);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(-i, -i);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
        }
        if(type == PieceType.ROOK) {
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(0, i);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(0, -i);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(i, 0);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(-i, 0);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
        }
        if(type == PieceType.QUEEN) {
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(0, i);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(0, -i);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(i, 0);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(-i, 0);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(i, i);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(i, -i);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(-i, i);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
            for(int i = 1; i < 8; i++){
                ChessPosition target = myPosition.addOffset(-i, -i);
                if (board.isValidMove(target, this.getTeamColor()) && myPosition != target) {
                    moves.add(new ChessMove(myPosition, target, null));
                    if(board.getPiece(target) != null){
                        break;
                    }
                }
                else {
                    break;
                }
            }
        }
        return moves;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(obj.getClass() != this.getClass()){
            return false;
        }
        ChessPiece p = (ChessPiece)obj;
        if(this.pieceColor == p.pieceColor && this.type == p.type){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return type.hashCode()*pieceColor.hashCode();
    }
}
