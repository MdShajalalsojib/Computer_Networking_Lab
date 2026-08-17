import java.io.*;
import java.net.*;

/**
 * HelpDeskClient.java
 *
 * Client program for the University Help Desk system.
 * Connects to the server, sends the user's name, then continuously
 * sends messages typed by the user and prints the server's replies,
 * until the user types "TATA" to close the connection.
 */
public class Client {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in))) {
            // Read the server's prompt for the name (e.g. "Enter your name:")
            System.out.println(in.readLine());
            String name = keyboard.readLine();
            out.println(name);

            // Read and display the welcome message
            System.out.println("Server: " + in.readLine());

            // Main loop: send messages typed by the user, print server's acknowledgment
            String userInput;
            while (true) {
                System.out.print(name + ": ");
                userInput = keyboard.readLine();
                if (userInput == null)
                    break;

                out.println(userInput);

                String response = in.readLine();
                if (response == null)
                    break;

                System.out.println("Server: " + response);

                if (userInput.equalsIgnoreCase("TATA")) {
                    break;
                }
            }

            System.out.println("Disconnected from the server.");

        } catch (IOException e) {
            System.out.println("Client exception: " + e.getMessage());
        }
    }
}