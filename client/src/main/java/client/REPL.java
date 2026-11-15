package client;

import java.util.Scanner;

public class REPL {
    private final ChessClient client;

    public REPL(int serverPort){
        this.client = new ChessClient(serverPort);
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
                if (!result.equals("quit")){
                    System.out.println(result);
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.println(msg);
            }
        }
        System.out.println("Exiting Chess Client");
    }

    private void printPrompt() {
        System.out.print("\n>>> ");
    }
}
