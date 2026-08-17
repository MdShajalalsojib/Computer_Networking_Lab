package open_ended;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    private String username;
    private int score;


    // Questions
    private String[] questions = {
        "Which protocol is connection-oriented?",
        "What does IP stand for?",
        "Which protocol is connectionless?",
        "Which device connects different networks?",
        "Which protocol is used to test network connectivity?"
    };


    // Options
    private String[][] options = {

        {"A. UDP", "B. TCP", "C. IP", "D. ICMP"},

        {"A. Internet Protocol",
         "B. Internal Protocol",
         "C. Internet Program",
         "D. Information Protocol"},

        {"A. TCP", "B. HTTP", "C. UDP", "D. FTP"},

        {"A. Switch", "B. Hub", "C. Router", "D. Repeater"},

        {"A. FTP", "B. Ping", "C. SMTP", "D. HTTP"}
    };


    // Correct answers
    private char[] correctAnswers = {
        'B',
        'A',
        'C',
        'C',
        'B'
    };


    // Constructor
    public ClientHandler(Socket socket) {

        this.socket = socket;

        try {

            input = new DataInputStream(
                    socket.getInputStream());

            output = new DataOutputStream(
                    socket.getOutputStream());

        }
        catch (IOException e) {

            System.out.println(
                    "Error creating input/output streams.");
        }
    }


    // Thread execution
    @Override
    public void run() {

        try {

            // =========================
            // LOGIN
            // =========================

            username = input.readUTF();

            String password = input.readUTF();


            if (!authenticate(username, password)) {

                output.writeUTF("LOGIN_FAILED");

                System.out.println(
                        "Login failed for: " + username);

                socket.close();

                return;
            }


            output.writeUTF("LOGIN_SUCCESS");

            System.out.println(
                    "Login successful: " + username);


            // =========================
            // START EXAM
            // =========================

            String command = input.readUTF();


            if (command.equals("START")) {

                startExam();
            }


            // =========================
            // CLOSE CONNECTION
            // =========================

            socket.close();

            System.out.println(
                    "Connection closed for: " + username);

        }
        catch (IOException e) {

            System.out.println(
                    "Connection error with client.");
        }
    }


    // =========================
    // AUTHENTICATION
    // =========================

    private boolean authenticate(
            String username,
            String password) {

        if (username.equals("student1")
                && password.equals("1234")) {

            return true;
        }


        if (username.equals("student2")
                && password.equals("1234")) {

            return true;
        }


        if (username.equals("student3")
                && password.equals("1234")) {

            return true;
        }


        return false;
    }


    // =========================
    // START EXAM
    // =========================

    private void startExam() throws IOException {

        score = 0;


        // Send total number of questions
        output.writeInt(questions.length);


        // Send questions one by one
        for (int i = 0; i < questions.length; i++) {

            output.writeUTF("QUESTION");

            // Send question
            output.writeUTF(questions[i]);


            // Send four options
            for (int j = 0; j < 4; j++) {

                output.writeUTF(options[i][j]);
            }


            // Receive student's answer
            String answer = input.readUTF();


            // Check answer
            if (answer.length() > 0) {

                char studentAnswer =
                        Character.toUpperCase(
                                answer.charAt(0));

                if (studentAnswer == correctAnswers[i]) {

                    score++;
                }
            }
        }


        // =========================
        // SUBMIT
        // =========================

        String command = input.readUTF();


        if (command.equals("SUBMIT")) {

            output.writeUTF("RESULT");

            output.writeInt(score);

            output.writeInt(questions.length);


            System.out.println(
                    username
                    + " completed exam. Score: "
                    + score
                    + "/"
                    + questions.length);
        }
    }
}