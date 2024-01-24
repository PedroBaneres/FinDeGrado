package com;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.PrintWriter;



public class ChatServidor {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6000);
        System.out.println("Servidor esperando conexiones...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

            // Crea un nuevo hilo para manejar la comunicación con el cliente
            Thread clientHandler = new Thread(new ClientHandler(clientSocket));
            clientHandler.start();

            // Crea un nuevo hilo para manejar la entrada del teclado y enviar mensajes al cliente
            Thread keyboardInputHandler = new Thread(() -> {
                try {
                    handleKeyboardInput(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            keyboardInputHandler.start();
        }
    }

    private static void handleKeyboardInput(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (true) {
                // Lee la entrada del teclado y envía al cliente
                String response = consoleInput.readLine();
                out.println(response);
            }
        } finally {
            out.close();
        }
    }
}