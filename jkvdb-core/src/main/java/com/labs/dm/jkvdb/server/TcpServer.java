package com.labs.dm.jkvdb.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author daniel
 * @since 28.04.2015
 */
public class TcpServer {

    public static void main(String argv[]) throws Exception {
        new TcpServer().runServer();
    }

    private void runServer() throws IOException {
        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(6789);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            System.out.println("Received: " + clientSentence);
            capitalizedSentence = clientSentence.toUpperCase() + '\n';
            outToClient.writeBytes(capitalizedSentence);
        }
    }

}
