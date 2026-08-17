package open_ended;

import java.io.*;
import java.net.*;

public class server {

    public static void main(String args[]) throws IOException {

        ServerSocket serversocket = new ServerSocket(5000);

        System.out.println("========================================");
        System.out.println("          NETWORK EXAM SERVER");
        System.out.println("========================================");
        System.out.println("Server started on port 5000");
        System.out.println("Waiting for students...");
        System.out.println();

        while (true) {

            Socket clientsocket = serversocket.accept();

            System.out.println("Student connected!");
            System.out.println("Client IP: " + clientsocket.getInetAddress());
            System.out.println("Client Port: " + clientsocket.getPort());

            ClientHandler clienthandler = new ClientHandler(clientsocket);

            clienthandler.start();

            System.out.println("ClientHandler thread started.");
            System.out.println("----------------------------------------");
        }
    }
}