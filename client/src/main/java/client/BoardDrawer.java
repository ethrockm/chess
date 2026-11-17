package client;

import chess.ChessGame;

public class BoardDrawer {

    private static final String RESET = "\u001b[0m";
    private static final String SET_BG_LIGHT = "\u001b[47m"; // White/Light background
    private static final String SET_BG_DARK = "\u001b[100m";  // Dark background
    private static final String SET_TEXT_RED = "\u001b[31m";   // White pieces
    private static final String SET_TEXT_BLUE = "\u001b[34m";  // Black pieces

    public static String drawBoard(chess.ChessBoard board, boolean whitePerspective) {
        StringBuilder sb = new StringBuilder();

        int startRow = whitePerspective ? 8 : 1;
        int endRow = whitePerspective ? 0 : 9;
        int rowStep = whitePerspective ? -1 : 1;

        int startCol = whitePerspective ? 1 : 8;
        int endCol = whitePerspective ? 9 : 0;
        int colStep = whitePerspective ? 1 : -1;

        sb.append("   ");
        for (int c = startCol; c != endCol; c+= colStep){
            sb.append(" ").append((char) ('a' + c -1)).append(" ").append(RESET);
        }
        sb.append("\n");

        for (int r = startRow; r != endRow; r += rowStep) {
            sb.append(" ").append(r).append(" ");
            for (int c = startCol; c != endCol; c += colStep) {
                chess.ChessPosition pos = new chess.ChessPosition(r, c);
                chess.ChessPiece piece = board.getPiece(pos);
                boolean isLightSquare = (r + c) % 2 != 0;
                String bg = isLightSquare ? SET_BG_LIGHT : SET_BG_DARK;
                String pieceChar = getPieceCharacter(piece);
                sb.append(bg).append(" ").append(pieceChar).append(" ").append(RESET);
            }
            sb.append(" ").append(r).append("\n");
        }
        sb.append("   ");
        for (int c = startCol; c != endCol; c += colStep){
            sb.append(" ").append((char) ('a' + c - 1)).append(" ").append(RESET);
        }
        sb.append("\n");
        return sb.toString();
    }

    private static String getPieceCharacter(chess.ChessPiece piece){
        if(piece == null) {
            return " ";
        }

        boolean isWhite = (piece.getTeamColor() == ChessGame.TeamColor.WHITE);
        String colorCode = isWhite ? SET_TEXT_RED : SET_TEXT_BLUE;

        String symbol = switch (piece.getPieceType()) {
            case PAWN -> "P";
            case KNIGHT -> "N";
            case BISHOP -> "B";
            case ROOK -> "R";
            case QUEEN -> "Q";
            case KING -> "K";
        };
        return colorCode + symbol;
    }

}
