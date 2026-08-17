import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * HelpDeskServer.java
 *
 * Multithreaded server for the University Help Desk system.
 * Accepts multiple clients simultaneously and spawns a dedicated
 * ClientHandler thread for every connected client so that no
 * client's communication blocks another client.
 */
public class Server {

    private static final int PORT = 5000;

    // Thread-safe set to keep track of all currently connected clients (for
    // logging/future use)
    private static final Set<String> connectedClients = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        System.out.println("Server started on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            int clientCount = 0;

            // Keep accepting new client connections forever
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                String defaultTag = "Client-" + clientCount;

                // Create and start a new thread to handle this client independently
                ClientHandler handler = new ClientHandler(clientSocket, defaultTag);
                Thread t = new Thread(handler);
                t.start();
            }

        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }

    /**
     * ClientHandler runs on its own thread for each connected client.
     * It handles the name handshake, message loop, and clean disconnection.
     */
    static class ClientHandler implements Runnable {

        private final Socket socket;
        private final String connectionTag; // e.g. Client-1, used before the real name is known
        private String clientName = "Unknown";

        public ClientHandler(Socket socket, String connectionTag) {
            this.socket = socket;
            this.connectionTag = connectionTag;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                System.out.println("Client connected: " + connectionTag);

                // Step 1: Ask for and read the client's name
                out.println("Enter your name:");
                String name = in.readLine();
                if (name == null || name.trim().isEmpty()) {
                    name = connectionTag;
                }
                clientName = name.trim();
                connectedClients.add(clientName);

                // Step 2: Send welcome message
                out.println("Welcome " + clientName + "! You are connected to the university help desk.");

                // Step 3: Continuously receive messages until client sends "TATA"
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("TATA")) {
                        out.println("Goodbye " + clientName + ". Your connection has been closed.");
                        break;
                    }

                    // Log message on server console
                    System.out.println(clientName + ": " + message);

                    // Send acknowledgment back to the client
                    out.println("Message received from " + clientName + ": " + message);
                }

            } catch (IOException e) {
                System.out.println(clientName + " connection error: " + e.getMessage());
            } finally {
                // Step 4: Clean up - close socket and remove client from active set
                connectedClients.remove(clientName);
                try {
                    socket.close();
                } catch (IOException e) {
                    // ignore close errors
                }
                System.out.println(clientName + " disconnected.");
                System.out.println("Server is still serving: " +
                        (connectedClients.isEmpty() ? "no other clients" : connectedClients));
            }
        }
    }
}