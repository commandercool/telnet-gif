package com.github.cc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.cc.client.ClientStreamer;
import com.github.cc.image.Animation;

public class TelnetGif {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(200);

    public static void main(String[] args) throws IOException {
        System.out.println("Init animation ...");
        Animation.init();
        System.out.println("Open socket ...");
        ServerSocket serverSocket = new ServerSocket(23);
        while (true) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("Connection from: " + client.getInetAddress());
                executorService.execute(new ClientStreamer(client));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
