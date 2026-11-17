package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        var port = 8080;
        if (args.length >= 1){
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port argument. Defaulting to 8080.");
            }
        }
        var repl = new REPL(port);
        repl.run();
        //var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        //System.out.println("♕ 240 Chess Client: " + piece);
    }
}
