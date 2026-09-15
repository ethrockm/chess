package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece[][] boardArray;
    public ChessBoard() {
        this.boardArray = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        boardArray[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return boardArray[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        boardArray[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    }

    /**
     *
     * @param target
     * @param teamColor
     * @return
     */
    public boolean isValidMove(ChessPosition target, ChessGame.TeamColor teamColor) {
        if (target.col > 7|| target.col < 0){
            return false;
        }
        if (target.row > 7|| target.row < 0){
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == this){
            return true;
        }
        if(obj.getClass() != this.getClass()){
            return false;
        }

        ChessBoard p = (ChessBoard) obj;
        if(this.boardArray.length != p.boardArray.length){
            return false;
        }
        if(this.boardArray == null || p.boardArray == null){
            return false;
        }

        for(int i = 0; i < this.boardArray.length; i++){
            ChessPiece[] thisRow = this.boardArray[i];
            ChessPiece[] pRow = p.boardArray[i];
            if(thisRow == pRow){
                continue;
            }
            if(thisRow == null || pRow == null){
                return false;
            }
            if(thisRow.length != pRow.length){
                return false;
            }
            for(int j = 0; j< thisRow.length; j++) {
                if (thisRow[j] != pRow[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        if(this.boardArray == null){
            return 0;
        }
        for (int i = 0; i < this.boardArray.length; i++){
            ChessPiece[] row = this.boardArray[i];
            int rowHash = 1;
            if(row != null){
                for(int j = 0; j < row.length; j++){
                    if(row[j] != null){
                        rowHash = 24 * rowHash + (row[j].hashCode() * 5);
                    }
                }
            }
            hash = 9 * hash + rowHash;
        }
        return hash;
    }
}
