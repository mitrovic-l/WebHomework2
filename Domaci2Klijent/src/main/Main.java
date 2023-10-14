package main;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static final int PORT = 9090;
    public static Socket socket = null;
    public static Thread readerThread = null;
    public static Thread writerThread = null;


    public static void main(String[] args) {
        try {
            socket = new Socket("127.0.0.1", PORT);
            readerThread = new Thread( new ReaderThread(socket));
            writerThread = new Thread( new WriterThread(socket));

            readerThread.start();
            writerThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
