package pl.edu.uws.lw89233;

import pl.edu.uws.lw89233.managers.DatabaseManager;
import pl.edu.uws.lw89233.managers.MessageManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register {

    private final int PORT = Integer.parseInt(System.getenv("REGISTRATION_MICROSERVICE_PORT"));
    private final String DB_HOST = System.getenv("DB_HOST");
    private final String DB_PORT = System.getenv("DB_PORT");
    private final String DB_NAME = System.getenv("DB_NAME");
    private final String DB_USER = System.getenv("DB_USER");
    private final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    private final DatabaseManager dbManager = new DatabaseManager(DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD);

    public void startService() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Registration microervice is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Error starting Registration microservice: " + e.getMessage());
        }
    }

    private class ClientHandler extends Thread {

        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request = in.readLine();
                if (request != null && request.contains("registration_request")) {
                    String response = handleRegistration(request);
                    out.println(response);
                }
            } catch (IOException e) {
                System.err.println("Error handling client request: " + e.getMessage());
            }
        }

        private String handleRegistration(String request) {
            MessageManager responseManager = new MessageManager(request);
            String login = responseManager.getAttribute("login");
            String password = responseManager.getAttribute("password");
            String response = "type:registration_response#message_id:%s#".formatted(responseManager.getAttribute("message_id"));

            String sql = "INSERT INTO users (login, password) VALUES (?, ?)";

            try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
                stmt.setString(1, login);
                stmt.setString(2, password);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    response += "status:200#";
                } else {
                    response += "#status:400#";
                }
                return response;

            } catch (SQLException e) {
                System.err.println("Error during registration: " + e.getMessage());
                response += "status:400#";
                return response;
            }
        }
    }

    public static void main(String[] args) {
        new Register().startService();
    }
}