package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MultiClientServer {
    private static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Server started. Waiting for clients...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clients.add(clientHandler);
            Thread clientThread = new Thread(clientHandler);
            clientThread.start();
        }
    }

    public static void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}
