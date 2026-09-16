package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    ChessPosition start;
    ChessPosition end;
    ChessPiece.PieceType promotion;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.start = startPosition;
        this.end = endPosition;
        this.promotion = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotion;
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
        ChessMove p = (ChessMove)obj;
        if(this.start == p.start && this.end == p.end){
            return true;
        }
        if(Objects.equals(this.start,p.start) && Objects.equals(this.end,p.end) && Objects.equals(this.promotion,p.promotion)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if(this.promotion == null){
            return start.hashCode()*(end.hashCode()+23);
        }
        return start.hashCode()*end.hashCode()*promotion.hashCode();
    }

    @Override
    public String toString() {
        return "ChessMove{" +
                "start=" + start +
                ", end=" + end +
                ", promotion=" + promotion +
                '}';
    }
}
