package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String inputLine;

            // Lee datos del cliente y muestra en la consola
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Cliente: " + inputLine); //aqui pa diferenciar clientes coger nombre
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}