package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatCliente {
        public static void main(String[] args) throws IOException {
            Socket socket = new Socket("localhost", 6000);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Crea un hilo para manejar la entrada del servidor
            Thread serverInputHandler = new Thread(() -> {
                try {
                    String serverInput;
                    while ((serverInput = in.readLine()) != null) {
                        System.out.println("Soporte: " + serverInput);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverInputHandler.start();
            // Lee datos del teclado y los envía al servidor
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while ((userInput = consoleInput.readLine()) != null) {
                out.println(userInput);
            }
            in.close();
            out.close();
            socket.close();
        }
    }
