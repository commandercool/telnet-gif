package com.github.cc.client;

import static com.github.cc.image.Animation.getFrames;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.github.cc.image.Animation;

public class ClientStreamer implements Runnable {

    private final Socket client;

    public ClientStreamer(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            OutputStream outputStream = client.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.println("\u001B[2J");
            int maxCycles = 4;
            while (!client.isClosed() && maxCycles-- > 0) {
                for (Animation.Frame frame : getFrames()) {
                    printWriter.print(frame.getAsciiImage());
                    printWriter.flush();
                    try {
                        Thread.sleep(frame.getDelayTime() * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    printWriter.println("\u001B[1H");
                }
            }
            printWriter.println("\u001B[2J");
            printWriter.println("You've been Rickrolled in ASCII style!");
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
