package client;

import java.util.Scanner;

public class REPL {
    private final ChessClient client;

    public REPL(int serverPort){
        client = new ChessClient(serverPort);
    }

    public void run() {
        System.out.println("Welcome to the chess client. Type 'help' to learn available commands.");

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void printPrompt() {
        System.out.print("\n>>> ");
    }
}
