package open_ended;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    public static void main(String args[]) throws IOException {


        // =========================
        // CONNECT TO SERVER
        // =========================

        Socket socket =
                new Socket("localhost", 5000);


        DataInputStream input =
                new DataInputStream(
                        socket.getInputStream());


        DataOutputStream output =
                new DataOutputStream(
                        socket.getOutputStream());


        Scanner scanner =
                new Scanner(System.in);


        System.out.println(
                "========================================");

        System.out.println(
                "          NETWORK EXAM SYSTEM");

        System.out.println(
                "========================================");


        // =========================
        // LOGIN
        // =========================

        System.out.print("Username: ");
        String username = scanner.nextLine();


        System.out.print("Password: ");
        String password = scanner.nextLine();


        // Send username and password
        output.writeUTF(username);
        output.writeUTF(password);


        // Receive login result
        String loginResult =
                input.readUTF();


        if (loginResult.equals("LOGIN_FAILED")) {

            System.out.println();
            System.out.println("Login failed!");
            System.out.println(
                    "Invalid username or password.");


            socket.close();
            scanner.close();

            return;
        }


        System.out.println();
        System.out.println("Login successful!");
        System.out.println(
                "Welcome, " + username);


        // =========================
        // START EXAM
        // =========================

        System.out.println();
        System.out.println(
                "Press ENTER to start the exam...");

        scanner.nextLine();


        output.writeUTF("START");


        // Receive total questions
        int totalQuestions =
                input.readInt();


        System.out.println();
        System.out.println(
                "========================================");

        System.out.println(
                "             EXAM STARTED");

        System.out.println(
                "========================================");

        System.out.println(
                "Total Questions: "
                + totalQuestions);


        // =========================
        // RECEIVE QUESTIONS
        // =========================

        for (int i = 0;
             i < totalQuestions;
             i++) {


            String questionCommand =
                    input.readUTF();


            if (questionCommand.equals("QUESTION")) {


                // Receive question
                String question =
                        input.readUTF();


                System.out.println();
                System.out.println(
                        "----------------------------------------");

                System.out.println(
                        "Question "
                        + (i + 1)
                        + " of "
                        + totalQuestions);

                System.out.println(
                        "----------------------------------------");


                System.out.println(question);


                // Receive and display options
                for (int j = 0; j < 4; j++) {

                    String option =
                            input.readUTF();

                    System.out.println(option);
                }


                // Get student's answer
                System.out.print(
                        "\nYour answer: ");

                String answer =
                        scanner.nextLine();


                // Send answer to server
                output.writeUTF(answer);
            }
        }


        // =========================
        // SUBMIT EXAM
        // =========================

        output.writeUTF("SUBMIT");


        // =========================
        // RECEIVE RESULT
        // =========================

        String resultCommand =
                input.readUTF();


        if (resultCommand.equals("RESULT")) {


            int score =
                    input.readInt();


            int total =
                    input.readInt();


            System.out.println();
            System.out.println(
                    "========================================");

            System.out.println(
                    "             EXAM RESULT");

            System.out.println(
                    "========================================");


            System.out.println(
                    "Student: " + username);


            System.out.println(
                    "Correct Answers: " + score);


            System.out.println(
                    "Wrong Answers: "
                    + (total - score));


            System.out.println(
                    "Score: "
                    + score
                    + "/"
                    + total);


            System.out.println(
                    "========================================");
        }


        // =========================
        // CLOSE CONNECTION
        // =========================

        socket.close();

        scanner.close();


        System.out.println();
        System.out.println(
                "Connection closed.");
    }
}